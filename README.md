# 🏦 LedgerFlow — Financial Core System

[![Java Version](https://img.shields.io/badge/Java-24.0.1-orange.svg)](https://www.oracle.com/java/technologies/downloads/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

O **LedgerFlow** é um sistema de motor financeiro desenvolvido em Java para simular operações bancárias de alta integridade. O projeto foca na aplicação rigorosa de **Programação Orientada a Objetos (POO)** e no uso das funcionalidades mais recentes da linguagem.

---

## 🚀 Funcionalidades Atuais
- **Gestão de Contas Multi-tipo:** Suporte a Conta Corrente e Poupança através de Herança e Polimorfismo, garantindo extensibilidade do sistema.
- **Log Transacional Imutável:** Cada operação gera um registro rastreável com IDs únicos (`UUID`) e carimbos de tempo (`LocalDateTime`), refletidos em tempo real nas contas envolvidas.
- **Data API & Filtros Inteligentes:** Motor de busca para geração de extratos segmentados por tipo de operação e intervalos temporais dinâmicos, utilizando processamento funcional.
- **Busca de Performance O(1):** Localização instantânea de correntistas e contas destino através de `HashMaps`, eliminando a necessidade de iterações lentas em grandes volumes de dados.
- **Blindagem e Documentação:** Lógica de negócio protegida por testes unitários rigorosos (JUnit 5) e código integralmente documentado no padrão profissional JavaDoc.
## 🛠️ Tecnologias e Conceitos Aplicados
- **Linguagem:** Java 24.0.1.
- **Arquitetura e Princípios:** POO Avançada (Abstração, Encapsulamento) e aplicação rigorosa de SOLID (especialmente o Princípio da Responsabilidade Única e Substituição de Liskov).
- **Processamento Funcional:** Uso estratégico de Streams API e Lambda expressions para otimização algorítmica e filtros de dados complexos.
- **Qualidade de Software:** Ciclo de desenvolvimento fundamentado em testes automatizados e tratamento de exceções customizadas para resiliência do core banking.
- **Gestão Ágil e Versionamento:** Fluxo gerenciado via Kanban (GitHub Projects) e histórico baseado em Conventional Commits para rastreabilidade semântica.

## 🔬 Pesquisa & Inovação (UFRRJ)
Como parte do meu projeto de **Iniciação Científica**, o LedgerFlow serve como laboratório para:
- **Graph-Based Fraud Detection:** Pesquisa teórica sobre a modelagem de transações como grafos direcionados para identificar ciclos suspeitos de movimentação de capital e anomalias financeiras.

## 🗺️ Roadmap de Evolução
✅ **Fase 1: Fundamentos e Lógica Bancária (Concluído)**
- Core Banking & POO: Implementação dos pilares de POO (Herança, Polimorfismo e Encapsulamento).
  
- Criação de transações imutáveis com registros de LocalDateTime e UUID.
  
- Tratamento de exceções customizadas para regras de negócio (InsufficientFundsException).
  
  
✅ **Fase 2: Arquitetura e Otimização Estrutural (Concluído)**
- Arquitetura em Camadas: Desacoplamento da Interface de Usuário (BancoUI) da lógica de orquestração.
  
- Robustez de Entrada: Blindagem contra falhas de terminal e Safe Input para garantir a resiliência do software.
  
- Otimização de Estrutura de Dados: Migração de List para HashMap, reduzindo a complexidade de busca de $O(n)$ para $O(1)$.
  
  
✅ **Fase 3: Processamento Moderno e Qualidade (Concluído)**
- Functional Programming: Implementação de Java Streams para filtragem avançada e eficiente do histórico de transações.

- Qualidade de Software: Introdução de Testes Unitários com JUnit 5 para garantir a integridade dos fluxos críticos de saque e transferência.

- Data API: Geração de extratos inteligentes segmentados por tipo de operação e intervalos temporais dinâmicos.
  
⏳ **Fase 4: Persistência e Integridade SQL (Em progresso)**
- Camada de Persistência: Substituição do armazenamento volátil (Map) por persistência física em banco de dados relacional.

- JDBC & MySQL: Integração direta com SQL para garantir a durabilidade e segurança dos dados sob os princípios ACID.

- Padrão DAO: Implementação do padrão Data Access Object para desacoplar a lógica de negócio do acesso aos dados.
  
📅 **Fase 5: Ecossistema Spring Boot e API REST**
- Migração de Framework: Transformação da aplicação CLI para um serviço web escalável utilizando o ecossistema Spring Boot.

- Arquitetura REST: Modelagem de endpoints para operações bancárias seguindo os métodos HTTP e boas práticas de design de API.

- Dependency Injection: Utilização do container do Spring para gerenciar o ciclo de vida e a injeção de dependências do sistema.

📅 **Fase 6: Segurança e Documentação Profissional**
- Spring Security: Implementação de camadas de autenticação e autorização para proteger os dados sensíveis dos correntistas.

- Swagger/OpenAPI: Documentação técnica interativa dos endpoints da API, facilitando a integração e o consumo do serviço.

- Logs e Monitoramento: Implementação de logs estruturados para rastreabilidade de transações e auditoria financeira.
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
