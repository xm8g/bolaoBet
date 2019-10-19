# bolaoBet

A Idéia...

* Criar um sistema de bolão que tenha os seguintes requisitos:

- [x] Permitir que o PERFIL como ADMIN crie um campeonato de futebol qualquer.
- [x] Criar os perfis ADMIN, USER, GESTOR e GUEST
- [x] Permitir que o PERFIL como ADMIN crie os clubes
- [x] Permitir o cadastro do usuário com login e senha. A senha deve ser encriptada ao ser gravada no banco.
- [x] No ato de um cadastro de um usuário será criado o perfil USER.
- [ ] Permitir que um usuário possa selecionar um campeonato e que possa criar um bolão a partir dele, tornando-se e USER e GESTOR.
- [ ] No papel de GESTOR, o mesmo poderá enviar convites aos emails dos interessados.
- [ ] Um usuário com perfil USER, poderá participar de um bolão ao qual foi convidado. Ele será notificado do convite ao entrar no sistema.
- [ ] Ao aceitar o convite o usuário passa a ter dois perfis, USER e GUEST
- [ ] Permitir ao GESTOR retirar o participante caso precise.
- [ ] Permitir ao GESTOR definir as regras de premiação.

* O sistema terá as seguintes regras de pontuação dos palpites do campeonato:
  1. placar exato      18 pontos
  2. gols do vencedor  13 pontos
  3. saldo do vencedor 11 pontos
  4. resultado          9 pontos
  5. gols do perdedor   5 pontos
  6. empate garantido   3 pontos

* O sistema terá apostas que poderão se converter em mais pontos para o competidor
* O sistema adotará os seguintes mercados:
    1. Over/Under gols
    2. Intervalo/Final
    3. BTS (Ambas Marcam)
    4. Escanteios

* O sistema de apostas se baseia em COINS que o usuário obtiver.
* Para se obter 1(um) COIN o usuário precisará:
    1. Acertar placar exato de um jogo
    2. A cada 200 pontos ganhos
    3. Sendo líder da rodada
    
* O usuário poderá multiplicar esses COINs em aposta em mercados paralelos a cada jogo
* O usuário pode colocar suas COINs, a qtde que quiser em apenas um mercado por jogo
* A COIN’s será multiplicada pela ODD em caso de acerto
* Ao final da rodada o usuário pode trocar os COINS (parte inteira) por pontos obedecidos os pesos da rodada.
* Na última rodada todos os COINS serão trocados por pontos, multiplicados pelo número da rodada.

* Tarefas do ADMIN do Site

- [ ] Cadastrar os jogos da rodada definindo todas os detalhes
- [ ] Habilitar os mercados que poderão ter apostas para aquele jogo
- [ ] Fazer uso da [ODDS API](https://the-odds-api.com) para definir as odds da categoria para cada jogo, pegar as odds uma hora antes do jogo. Se não cadastrá-las no dia do jogo.
- [ ] Processará os resultados dos palpites e apostas a qualquer momento

* Uma partida ou aposta após ser processada deverá ser marcada para resolvida e não poderá mais ser avaliada.

* O GUEST poderá:

  1. Ver a classificação da rodada.
  2. Ver a classificação geral.
  3. Ver seus ganhos de palpites (paginado)
  4. Ver seus ganhos de apostas (paginado)
  5. A tabela deve mostrar os pontos e coins de cada participante
  6. Ver os ganhos de todos na rodada

* O sistema deverá ser desenvolvido com:
  1. SpringBoot
  2. SpringSecurity
  3. MySql
  4. Deploy no heroku.
  5. Utilizará o thymeleaf com layout