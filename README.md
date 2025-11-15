# TDE03 - Performance em Sistemas Ciberfísicos

## Integrantes
- Lucas Brisch Zanlorenzi  
- Livia Rosembach de Oliveira  
- Giovani Nota Simões  

## Link do Vídeo
[*(Vídeo Explicando o Trabalho)*](https://youtu.be/eMqRHOkGPWg)

---

## 📁 Estrutura do Projeto

### Parte 1 - Problema dos Filósofos (Deadlock)
**Localização:** [`Parte 1 - Filosofos Pseudocódigo.md`](Parte%201%20-%20Filosofos%20Pseudocódigo.md)

Este arquivo contém:
- Análise da dinâmica do problema dos filósofos
- Explicação detalhada sobre como o deadlock ocorre
- Relação com as condições de Coffman
- Solução proposta usando hierarquia de garfos
- Pseudocódigo da solução

### Parte 2 - Condições de Corrida (Race Conditions)
**Localização:** [`src/raceConditions/`](src/raceConditions/)

Arquivos principais:
- [`CorridaSemControle.java`](src/raceConditions/CorridaSemControle.java) - Demonstração do problema de race condition sem sincronização
- [`CorridaComSemaphore.java`](src/raceConditions/CorridaComSemaphore.java) - Solução usando semáforo binário justo
- [`SyncVsNoSyncRaceCondition.java`](src/raceConditions/SyncVsNoSyncRaceCondition.java) - Comparação entre as duas abordagens
- [`Análise-Trade-off-Semáforo.md`](src/raceConditions/Análise-Trade-off-Semáforo.md) - Análise detalhada do trade-off entre corretude e desempenho

**Principais conceitos demonstrados:**
- Race conditions e perda de incrementos
- Uso de semáforos para exclusão mútua
- Garantias happens-before
- Trade-off entre desempenho e corretude

### Parte 3 - Deadlock em Código
**Localização:** [`src/deadlock/`](src/deadlock/)

Arquivos:
- [`DeadlockDemo.java`](src/deadlock/DeadlockDemo.java) - Demonstração prática de deadlock e sua solução

**Conceitos demonstrados:**
- Criação de deadlock usando synchronized e dois locks
- Solução através da ordenação consistente de locks
- Uso correto de monitores em Java
