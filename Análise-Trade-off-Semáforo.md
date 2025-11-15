# Análise do Trade-off: Semáforo vs. Condição de Corrida

## Introdução

Este documento analisa o trade-off entre **corretude** e **desempenho** ao usar semáforos para eliminar condições de corrida em sistemas concorrentes. Os resultados demonstram claramente o custo de garantir sincronização adequada em ambientes multi-threaded.

---

## Resultados dos Benchmarks

### Versão Sem Sincronização (Race Condition)
```
Esperado=2000000, Obtido=376077, Tempo=0.057s
```

### Versão Com Semáforo (Sincronizada)
```
Esperado=2000000, Obtido=2000000, Tempo=5.931s
```

---

## Análise Detalhada

### 1. **Corretude vs. Desempenho**

#### Sem Sincronização (raceConditions.CorridaSemControle)
- **Corretude**: ❌ Falha catastrófica
  - Esperado: 2.000.000 incrementos
  - Obtido: 376.077 (apenas **18,8%** do esperado)
  - **Perda de 81,2%** dos incrementos devido à condição de corrida

- **Desempenho**: ✅ Extremamente rápido
  - Tempo: **0.057 segundos**
  - Throughput altíssimo, mas resultados inválidos

#### Com Semáforo (raceConditions.CorridaComSemaphore)
- **Corretude**: ✅ 100% preciso
  - Esperado: 2.000.000 incrementos
  - Obtido: 2.000.000 (exatamente o esperado)
  - Zero perda de incrementos

- **Desempenho**: ⚠️ Significativamente mais lento
  - Tempo: **5.931 segundos**
  - **104x mais lento** que a versão sem sincronização

---

## Por Que a Condição de Corrida Acontece?

### O Problema do `count++`

A operação `count++` **não é atômica**. Ela envolve três etapas:

1. **Ler** o valor atual de `count` da memória
2. **Incrementar** o valor localmente
3. **Escrever** o novo valor de volta na memória

### Cenário de Corrida (Race Condition)

```
Thread A lê count = 100
Thread B lê count = 100  ← Ambas leem antes de qualquer escrita!
Thread A incrementa para 101
Thread B incrementa para 101
Thread A escreve 101
Thread B escreve 101  ← Perdemos um incremento!
```

**Resultado**: Dois incrementos executados, mas `count` aumentou apenas em 1.

### Fluxo Read-Modify-Write Corrompido

Com 8 threads competindo simultaneamente por acesso ao mesmo contador, a probabilidade de múltiplas threads lerem o mesmo valor antes de escrever é extremamente alta, resultando em perda massiva de incrementos.

---

## Como o Semáforo Resolve o Problema

### Exclusão Mútua com `Semaphore(1, true)`

```java
sem.acquire();  // Apenas 1 thread por vez pode passar
count++;        // Seção crítica protegida
sem.release();  // Libera para a próxima thread
```

### Garantias Fornecidas

1. **Exclusão Mútua**: Apenas uma thread executa `count++` por vez
2. **Serialização**: O fluxo read-modify-write é atômico
3. **Happens-Before**: Garante visibilidade de memória entre threads
4. **Fair Mode (FIFO)**: Threads adquirem o semáforo na ordem de requisição

---

## O Custo da Sincronização

### Por Que é 104x Mais Lento?

1. **Contenção de Lock**: 8 threads competindo por 1 único semáforo
   - Threads ficam bloqueadas esperando sua vez
   - Apenas 1 thread trabalha de cada vez = paralelismo zero

2. **Overhead do Semáforo**:
   - Custo de `acquire()` e `release()` em cada iteração (2.000.000 vezes)
   - Gerenciamento da fila FIFO (fair mode)
   - Context switching entre threads

3. **Perda de Throughput**:
   - Na versão sem sync: **8 threads em paralelo** (mesmo que incorreto)
   - Com semáforo: **serialização completa** = equivalente a 1 thread

### Cálculo de Throughput

- **Sem Sync**: ~35 milhões de incrementos/segundo (mas 81% perdidos)
- **Com Semáforo**: ~337 mil incrementos/segundo (mas 100% corretos)

---

## Garantias Happens-Before

### O Que É Happens-Before?

A relação **happens-before** garante que:
- Toda escrita feita por uma thread **antes de `release()`**
- É **visível** para qualquer thread **após `acquire()`**

### Na Prática

```java
Thread A:
  count = 5;      // Escrita
  sem.release();  // <- Release point

Thread B:
  sem.acquire();  // <- Acquire point
  int x = count;  // x será 5 (garantido!)
```

Sem happens-before, `Thread B` poderia ver um valor desatualizado de `count` devido a:
- Cache de CPU
- Reordenação de instruções
- Buffers de escrita

**O semáforo sincroniza não apenas o acesso, mas também a memória.**

---

## Trade-off: Quando Vale a Pena?

### Use Semáforo Quando:
✅ **Corretude é obrigatória** (resultados errados são inaceitáveis)  
✅ A seção crítica é **pequena e rápida**  
✅ Você precisa de **garantias de visibilidade** entre threads  
✅ Fair mode é necessário para evitar starvation  
