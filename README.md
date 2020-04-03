# bolaoBet

A Idéia...

* Criar um sistema de bolão que tenha os seguintes requisitos:

- [x] Permitir que o PERFIL como ADMIN crie um campeonato de futebol qualquer.
- [x] Criar os perfis ADMIN, USER, GESTOR e GUEST
- [x] Permitir que o PERFIL como ADMIN crie os clubes
- [x] Permitir o cadastro do usuário com login e senha. A senha deve ser encriptada ao ser gravada no banco.
- [x] No ato de um cadastro de um usuário será criado o perfil USER.
- [x] Disponibilizar na Home Page os Campeonatos que podem ser usufruidos para geração de um bolão.
- [x] Permitir que um usuário possa selecionar um campeonato e que possa criar um bolão a partir dele, tornando-se e USER e GESTOR.
- [x] No papel de GESTOR, o mesmo poderá enviar convites aos emails dos interessados.
- [x] Um usuário com perfil USER, poderá participar de um bolão ao qual foi convidado. Ele será notificado do convite ao entrar no sistema.
- [ ] Permitir ao GESTOR retirar o participante caso precise.

* O sistema terá as seguintes regras de pontuação dos palpites do campeonato:
  1. placar exato      18 pontos
  2. gols do vencedor  13 pontos
  3. saldo do vencedor 11 pontos
  4. resultado          9 pontos
  5. empate garantido   5 pontos

* O sistema terá apostas que poderão se converter em mais pontos para o competidor
* O sistema adotará os seguintes mercados:
    1. Over/Under gols FT
    2. Over/Under gols HT
    3. BTS (Ambas Marcam)
    4. Escanteios

* O sistema de apostas se baseia em COINS que o usuário obtiver.
* Para se obter 1(um) COIN o usuário precisará:
    1. Acertar placar exato de um jogo
    2. A cada 200 pontos ganhos apenas com os palpites
    3. Dependendo da colocação na rodada rodada.
	Exemplo: Se o bolão tem 10 participantes, o líder da rodada ganhará 10 coins, o segundo 9 coins e assim até o último ganhar 1 coin
    
* O usuário poderá multiplicar esses COINs em aposta em mercados paralelos a cada jogo
* O usuário pode colocar suas COINs, a qtde que quiser em apenas um mercado por jogo
* A COIN’s será multiplicada pela ODD em caso de acerto
* Ao final da rodada o usuário pode trocar os COINS multiplicado pelo numero da rodada, a parte decimal será desprezada.
* Pontos trocados não valem para obtenção de novos COINS
* Na última rodada todos os COINS serão trocados por pontos, multiplicados pelo número da rodada.

* Tarefas do ADMIN do Site

- [x] Cadastrar os jogos da rodada definindo todas os detalhes
- [ ] Habilitar os mercados que poderão ter apostas para aquele jogo
- [ ] Fazer uso da [ODDS API](https://the-odds-api.com) para definir as odds da categoria para cada jogo, pegar as odds no dia do jogo. Se não cadastrá-las no dia do jogo.
- [ ] Processará os resultados dos palpites e apostas a cada partida
- [x] A cada resultado processado, a classificação será atualizada para visualização de todos
- [ ] As apostas também serão processadas ao se colocar o placar do jogo nos dois tempos
- [ ] No header o jogador poderá ver seus pontos e seu saldo de COINS

* Uma partida ou aposta após ser processada deverá ser marcada para resolvida e não poderá mais ser avaliada.

* O GUEST poderá:

- [ ]  1. Ver a classificação da rodada.
- [x]  2. Ver a classificação geral.
- [x]  3. Ver seus ganhos de palpites
- [ ]  4. Ver seus ganhos de apostas (paginado)
- [x]  5. A tabela deve mostrar os pontos e coins de cada participante
- [x]  6. Ver um gráfico em linha da evolução
  
* O sistema deverá ser desenvolvido com:
  1. SpringBoot
  2. SpringSecurity
  3. MySql
  4. Deploy no heroku.
  5. Utilizará o thymeleaf com layout
