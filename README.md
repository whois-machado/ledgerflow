# 🏦 LedgerFlow — Financial Core System

[![Java Version](https://img.shields.io/badge/Java-24.0.1-orange.svg)](https://www.oracle.com/java/technologies/downloads/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

O **LedgerFlow** é um sistema de motor financeiro desenvolvido em Java para simular operações bancárias de alta integridade. O projeto foca na aplicação rigorosa de **Programação Orientada a Objetos (POO)** e no uso das funcionalidades mais recentes da linguagem.

---

## 🚀 Funcionalidades Atuais
- **Gestão de Contas:** Suporte a Conta Corrente (com cheque especial) e Poupança (com rendimentos) via Herança e Polimorfismo.
- **Log Transacional Consistente:** Cada operação gera um registro imutável com IDs únicos (`UUID`) e carimbos de tempo (`LocalDateTime`), refletidos simultaneamente nas contas de origem e destino.
- **Segurança de Saldo:** Lógica de transferência protegida contra registros inconsistentes e validação de fundos em tempo real.
- **Interface CLI:** Menu interativo via console para simulação de operações bancárias completas.

## 🛠️ Tecnologias e Conceitos Aplicados
- **Linguagem:** Java 24.0.1.
- **Arquitetura:** POO Avançada (Abstração, Encapsulamento, Polimorfismo).
- **Estrutura Profissional:** Organização por pacotes (`app`, `enums`, `model`) para escalabilidade.
- **Identificadores:** Uso de `UUID` para garantir a rastreabilidade universal de transações.

## 🔬 Pesquisa & Inovação (UFRRJ)
Como parte do meu projeto de **Iniciação Científica**, o LedgerFlow serve como laboratório para:
- **Graph-Based Fraud Detection:** Pesquisa teórica sobre a modelagem de transações como grafos direcionados para identificar ciclos suspeitos de movimentação de capital e anomalias financeiras.

## 🗺️ Roadmap de Evolução
- [x] Arquitetura Base e POO.
- [x] Lógica de Transferência Consistente.
- [ ] **[Próximo]** Implementação de Exceções Customizadas (`InsufficientFundsException`).
- [ ] Persistência de dados em memória via `Maps` e Streams.
- [ ] Integração com Banco de Dados SQL via JDBC.
- [ ] **Fase Spring:** Transformação em uma API REST escalável com Spring Boot.

---

## ⚙️ Como executar

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/whois-machado/ledgerflow.git](https://github.com/whois-machado/ledgerflow.git)
2. **Compile o projeto (a partir da raiz):**
   ```bash
   javac -d out src/com/ledgerflow/**/*.java
4. **Execute o Sistema:**
   ```bash
   java -cp out com.ledgerflow.app.AppBanco

---
Desenvolvido por **Gabriel Machado** — Engenheiro de Software & Estudante de Ciência da Computação (UFRRJ).
