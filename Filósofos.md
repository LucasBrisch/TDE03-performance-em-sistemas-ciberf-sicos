# RELATÓRIO

## DINÂMICA

Em uma mesa circular, existem 5 filósofos, todos alternando entre:

- Pensando()
- ComFome()
- Comendo(GarfoDireito, GarfoEsquerdo)

Repare que, para comer, um filósofo precisa dos 2 garfos ao seu lado. Esses garfos são compartilhados entre os filósofos, podendo ser utilizados apenas por um filósofo de cada vez.  
Esse problema pode apresentar situações de exclusão mútua e concorrência.

---

## DEADLOCK

O deadlock ocorre no protocolo ingênuo:

**“Cada filósofo pega primeiro o garfo da esquerda, depois o da direita”.**

Nesse caso:

1. Todos vão pegar o garfo esquerdo ao mesmo tempo.  
2. Ao tentar pegar o garfo da direita, ele estará ocupado pelo seu vizinho.  
3. Nenhum dos filósofos vai querer liberar o garfo que já está com ele, pois está esperando o segundo garfo para concluir sua ação de comer.

Forma-se um ciclo de dependências onde cada filósofo segura um recurso e espera outro recurso que está na mão de um vizinho que, por sua vez, também está esperando outro recurso.  
Isso leva a um bloqueio que impede todos eles de comer.

---

## RELAÇÃO COM COFFMAN

1. **Exclusão mútua** – os garfos não podem ser compartilhados simultaneamente  
2. **Hold and wait** – o filósofo não solta um garfo enquanto não tiver o outro  
3. **Não preempção** – não é possível “roubar” um garfo do filósofo ao lado  
4. **Espera circular** – todos estão esperando recursos que estão em outros processos  

Todas as 4 condições são verdadeiras no cenário apresentado, e se todas as 4 são verdadeiras, um deadlock pode acontecer.

---

## SOLUÇÃO PROPOSTA

Adicionar uma hierarquia nos garfos: cada garfo recebe um número, e o filósofo sempre pega primeiro o garfo de número menor.  
Seguindo essa ordem fixa, nenhum filósofo pode ficar segurando um garfo de número maior enquanto espera por um de número menor, o que impede a formação de uma espera circular, já evitando o deadlock.

---

## PSEUDOCÓDIGO

N = 5
garfos numerados de 0 até N-1
cada garfo fica entre o filosofo x e o filosofo (x+1 mod N) (Mod serve para o garfo 4 ficar entre o filosofo 4 e 0)

loop para cada filosofo f:

    pensar()
    estado[f] = "com fome"

    Garfo1 = adquirir(GarfoMenor)
    Garfo2 = adquirir(GarfoMaior)

    estado[f] = "comendo"
    comer(Garfo1, Garfo2)

    liberar(Garfo2)
    liberar(Garfo1)

    estado[f] = "pensando"

---

## CÓDIGO NA PRÁTICA

O mapeamento dos garfos e filósofos fica assim:

- Filósofo 0 → garfos 4 e 0  
- Filósofo 1 → garfos 0 e 1  
- Filósofo 2 → garfos 1 e 2  
- Filósofo 3 → garfos 2 e 3  
- Filósofo 4 → garfos 3 e 4  

Cada filósofo pega o seu garfo de menor número (independente do lado que ele esteja):

- Filósofo 0 → menor é 0  
- Filósofo 1 → menor é 0  
- Filósofo 2 → menor é 1  
- Filósofo 3 → menor é 2  
- Filósofo 4 → menor é 3  

Perceba que:

- Só dois filósofos tentam pegar o mesmo primeiro garfo (porque cada garfo é compartilhado por apenas dois filósofos).  
- Ninguém tenta pegar o garfo 4 como primeiro (porque ele é sempre o maior), então o garfo 4 nunca é a primeira escolha de ninguém.

Perceba também que o garfo 0 é disputado por 2 filósofos, mas isso não interfere no funcionamento do código: independente de quem pegar ele primeiro, alguém vai poder comer.

Se o filósofo 0 pegar o garfo 0, ele vai disputar o garfo 4 com o filósofo 4, que também já vai ter o seu primeiro garfo em mãos. Independente de qual pegar o garfo 4, um dos dois vai comer e liberar ambos os garfos para os seus vizinhos.

Se o filósofo 1 pegar o garfo 0, a única diferença é que o filósofo 4 vai pegar o garfo 4 sem ter que disputar.

O importante é que o filósofo 4 sempre vai conseguir comer, e vai acabar liberando os garfos adiante para os filósofos ao seu lado, até que todos tenham comido.
Esse comportamento ja impede que haja um deadlock caso todos queiram comer ao mesmo tempo

