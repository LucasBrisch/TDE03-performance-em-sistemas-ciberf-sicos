# Explicação da Estratégia de Prevenção de Deadlock

No código, a forma que usamos para evitar o deadlock foi definir uma ordem fixa para adquirir os locks das contas. Ou seja, independentemente da operação, todas as threads sempre seguem a mesma hierarquia de recursos (por exemplo, primeiro `LOCK_A`, depois `LOCK_B`).

## Como isso remove a espera circular

Ao definir uma ordem fixa para adquirir os locks, a espera circular deixa de existir. O deadlock depende de um ciclo onde cada thread segura um recurso e espera por outro, mas quando todas seguem a mesma hierarquia, esse ciclo não pode se formar. Essa lógica é exatamente a mesma da solução por hierarquia de recursos no problema dos filósofos: cada filósofo sempre pega primeiro o garfo de menor número e depois o de maior, eliminando a possibilidade de um ciclo de espera entre eles. Nesse caso, as contas assumem o papel dos garfos; ao impor uma ordem global de acesso, evitamos que uma thread fique com um recurso “mais alto” esperando por um “mais baixo”, o que quebra a condição de circularidade e impede o deadlock.

## Relação com o manter-e-esperar

Mesmo que ainda exista manter-e-esperar (uma thread segurando um lock enquanto tenta pegar outro), esse padrão deixa de ser perigoso justamente porque a ordem de aquisição é sempre a mesma para todas. Assim, não surgem dependências conflitantes e cada thread só tenta subir na hierarquia, nunca descer.

## Resumo

Ao aplicar uma hierarquia fixa para os locks, quebramos a condição de espera circular, acontece o contrle do manter-e-esperar e utilizamos a mesma lógica da solução clássica dos filósofos. Isso garante que não exista possibilidade de deadlock mesmo com múltiplas threads tentando acessar os mesmos recursos.
