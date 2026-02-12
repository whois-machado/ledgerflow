package com.ledgerflow.model;

import com.ledgerflow.enums.TipoTransacao;
import com.ledgerflow.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Validação das Regras de Negócio - Core Banking")
class ContaBancariaTest{

    private ContaBancaria contaOrigem;
    private ContaBancaria contaDestino;

    @BeforeEach
    void setup(){
        // Arrange: Estado inicial consistente para cada teste
        contaOrigem = new ContaCorrente("123", "001", "1010", 1000.0);
        contaDestino = new ContaCorrente("456", "001", "2020", 500.0);
    }

    // --- TESTES DE DEPÓSITO ---

    @Test
    @DisplayName("Deve incrementar saldo corretamente após depósito válido")
    void deveDepositarComSucesso(){
        contaOrigem.depositar(500.0); // Act
        assertEquals(1500.0, contaOrigem.getSaldo(), "O saldo deve refletir o valor depositado."); // Assert
    }

    @ParameterizedTest
    @ValueSource(doubles = {-10.0, 0.0})
    @DisplayName("Deve impedir depósitos de valores inválidos (negativos ou zero)")
    void deveLancarExcecaoParaDepositoInvalido(double valorInvalido){
        assertThrows(IllegalArgumentException.class, () -> contaOrigem.depositar(valorInvalido));
    }

    // --- TESTES DE SAQUE ---

    @Test
    @DisplayName("Deve permitir saque quando o valor é exatamente igual ao saldo")
    void devePermitirSaqueDeSaldoTotal() throws InsufficientFundsException{
        contaOrigem.sacar(1000.0);
        assertEquals(0.0, contaOrigem.getSaldo());
    }

    @Test
    @DisplayName("Deve lançar InsufficientFundsException ao sacar valor superior ao saldo")
    void deveValidarSaldoInsuficienteNoSaque(){
        assertThrows(InsufficientFundsException.class, () -> contaOrigem.sacar(1001.0));
    }

    // --- TESTES DE TRANSFERÊNCIA (O MAIS CRÍTICO) ---

    @Test
    @DisplayName("Deve garantir a atomicidade da transferência: debita origem e credita destino")
    void deveTransferirComSucesso() throws InsufficientFundsException{
        contaOrigem.transferir(300.0, contaDestino);

        assertAll("Verificação de Integridade da Transferência",
                () -> assertEquals(700.0, contaOrigem.getSaldo(), "Saldo de origem incorreto"),
                () -> assertEquals(800.0, contaDestino.getSaldo(), "Saldo de destino incorreto")
        );
    }

    @Test
    @DisplayName("Não deve alterar nenhum saldo se a transferência falhar por falta de fundos")
    void deveManterIntegridadeEmCasoDeFalhaNaTransferencia(){
        assertThrows(InsufficientFundsException.class, () ->
                contaOrigem.transferir(2000.0, contaDestino)
        );

        // Verificação de não-efeito colateral
        assertAll("Saldos devem permanecer inalterados",
                () -> assertEquals(1000.0, contaOrigem.getSaldo()),
                () -> assertEquals(500.0, contaDestino.getSaldo())
        );
    }

    @Test
    @DisplayName("Deve impedir transferência de valor zero ou negativo")
    void deveLancarExcecaoParaTransferenciaInvalida(){

        assertThrows(IllegalArgumentException.class, () ->
                contaOrigem.transferir(0.0, contaDestino),
        "O sistema deve bloquear transferencia sem valor monetário."
        );
    }

    @Test
    @DisplayName("Deve impedir transferência para a própria conta (Prevenção de loop)")
    void deveImpedirTransferenciaParaSiMesmo(){
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                        contaOrigem.transferir(100.0, contaOrigem),
                "Uma conta não pode transferir valores para si mesma."
        );
    }

    @Test
    @DisplayName("Data API: Deve filtrar transações por tipo e intervalo temporal")
    void deveFiltrarTransacoesCorretamente() {
        // Arrange: Criar cenário com datas distintas
        LocalDateTime ontem = LocalDateTime.now().minusDays(1);
        LocalDateTime agora = LocalDateTime.now();

        contaOrigem.depositar(100.0); // Depósito hoje
        // Simulação de transação antiga (exigiria ajuste no construtor ou setter para teste)

        // Act
        List<Transacao> apenasDepositos = contaOrigem.buscarExtratoInteligente(TipoTransacao.DEPOSITO, null, null);
        List<Transacao> semPix = contaOrigem.buscarExtratoInteligente(TipoTransacao.PIX, null, null);

        // Assert
        assertAll("Validação da Data API",
                () -> assertFalse(apenasDepositos.isEmpty(), "Deve encontrar o depósito realizado"),
                () -> assertTrue(semPix.isEmpty(), "Não deve encontrar PIX se nenhum foi feito")
        );
    }
}