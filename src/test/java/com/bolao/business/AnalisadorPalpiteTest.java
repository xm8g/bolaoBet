package com.bolao.business;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bolao.entity.bets.Palpite;
import com.bolao.entity.bets.domain.MotivoPonto;
import com.bolao.entity.jogo.Partida;
import com.bolao.entity.jogo.ResultadoPartida;

public class AnalisadorPalpiteTest {

	private Partida partidaMandanteVence;
	
	private Partida partidaEmpate;
	
	private Partida partidaVisitanteVence;
	
	private Palpite palpite;
	
	final AnalisadorPalpite analisaSeCravou = new AnalisadorPalpiteCravada();
	final AnalisadorPalpite analisaSeAcertouGolsDoVencedor = new AnalisadorPalpiteGolsVencedor();
	final AnalisadorPalpite analisaSeAcertouSaldo = new AnalisadorPalpiteSaldoGols();
	final AnalisadorPalpite analisaResultadoSimples = new AnalisadorPalpiteResultadoSimples();
	final AnalisadorPalpite analisaEmpateGarantido = new AnalisadorPalpiteEmpateGarantido();
	
	@Before
	public void setUp() throws Exception {
		
		//Resultado 1 x 1
		partidaEmpate = new Partida();
		ResultadoPartida resultadoPartidaEmpate = new ResultadoPartida();
		resultadoPartidaEmpate.setGolsHTMandante(0);
		resultadoPartidaEmpate.setGolsFTMandante(1);
		resultadoPartidaEmpate.setGolsHTVisitante(0);
		resultadoPartidaEmpate.setGolsFTVisitante(1);
		partidaEmpate.setResultado(resultadoPartidaEmpate);
		
		
		//Resultado 3 x 1
		partidaMandanteVence = new Partida();
		ResultadoPartida resultadoPartidaMandanteVenceu = new ResultadoPartida();
		resultadoPartidaMandanteVenceu.setGolsHTMandante(2);
		resultadoPartidaMandanteVenceu.setGolsFTMandante(1);
		resultadoPartidaMandanteVenceu.setGolsHTVisitante(0);
		resultadoPartidaMandanteVenceu.setGolsFTVisitante(1);
		partidaMandanteVence.setResultado(resultadoPartidaMandanteVenceu);
		

		//Resultado 0 x 2
		partidaVisitanteVence = new Partida();
		ResultadoPartida resultadoPartidaVisitante = new ResultadoPartida();
		resultadoPartidaVisitante.setGolsHTMandante(0);
		resultadoPartidaVisitante.setGolsFTMandante(0);
		resultadoPartidaVisitante.setGolsHTVisitante(0);
		resultadoPartidaVisitante.setGolsFTVisitante(2);
		partidaVisitanteVence.setResultado(resultadoPartidaVisitante);

		
		
		analisaSeCravou.proximaAnalise(analisaSeAcertouGolsDoVencedor);
		analisaSeAcertouGolsDoVencedor.proximaAnalise(analisaSeAcertouSaldo);
		analisaSeAcertouSaldo.proximaAnalise(analisaResultadoSimples);
		analisaResultadoSimples.proximaAnalise(analisaEmpateGarantido);
			
	}

	@Test
	public void testCravada() {
		
		//Cenário Mandante Vence
		palpite = new Palpite();
		palpite.setGolsMandante(3);
		palpite.setGolsVisitante(1);
		
		ResultadoAnalisePalpite resultadoAnalisePalpite1 = analisaSeCravou.acertouNestaCategoria(palpite, partidaMandanteVence.getResultado());
		
		Assert.assertThat(resultadoAnalisePalpite1.getPontos(), CoreMatchers.is(MotivoPonto.EXATO.getPontos()));
		Assert.assertThat(resultadoAnalisePalpite1.getMotivo(), CoreMatchers.equalTo(MotivoPonto.EXATO));
		
		//Cenário Visitante Vence
		palpite = new Palpite();
		palpite.setGolsMandante(0);
		palpite.setGolsVisitante(2);
				
		ResultadoAnalisePalpite resultadoAnalisePalpite2 = analisaSeCravou.acertouNestaCategoria(palpite, partidaVisitanteVence.getResultado());
				
		Assert.assertThat(resultadoAnalisePalpite2.getPontos(), CoreMatchers.is(MotivoPonto.EXATO.getPontos()));
		Assert.assertThat(resultadoAnalisePalpite2.getMotivo(), CoreMatchers.equalTo(MotivoPonto.EXATO));
	}
	
	@Test
	public void testGolsDoVencedor() {
		
		//Cenário Mandante Vence
		palpite = new Palpite();
		palpite.setGolsMandante(3);
		palpite.setGolsVisitante(0);
		
		ResultadoAnalisePalpite resultadoAnalisePalpite1 = analisaSeCravou.acertouNestaCategoria(palpite, partidaMandanteVence.getResultado());
		
		Assert.assertThat(resultadoAnalisePalpite1.getPontos(), CoreMatchers.is(MotivoPonto.GOLS_VENCEDOR.getPontos()));
		Assert.assertThat(resultadoAnalisePalpite1.getMotivo(), CoreMatchers.equalTo(MotivoPonto.GOLS_VENCEDOR));
		
		//Cenário Visitante Vence
		palpite.setGolsMandante(1);
		palpite.setGolsVisitante(2);
		
		ResultadoAnalisePalpite resultadoAnalisePalpite2 = analisaSeCravou.acertouNestaCategoria(palpite, partidaVisitanteVence.getResultado());
		
		Assert.assertThat(resultadoAnalisePalpite2.getPontos(), CoreMatchers.is(MotivoPonto.GOLS_VENCEDOR.getPontos()));
		Assert.assertThat(resultadoAnalisePalpite2.getMotivo(), CoreMatchers.equalTo(MotivoPonto.GOLS_VENCEDOR));
	}
	
	@Test
	public void testSaldo() {
		
		//Cenário Mandante Vence
		palpite = new Palpite();
		palpite.setGolsMandante(2);
		palpite.setGolsVisitante(0);
		
		ResultadoAnalisePalpite resultadoAnalisePalpite1 = analisaSeCravou.acertouNestaCategoria(palpite, partidaMandanteVence.getResultado());
		
		Assert.assertThat(resultadoAnalisePalpite1.getPontos(), CoreMatchers.is(MotivoPonto.SALDO_GOLS.getPontos()));
		Assert.assertThat(resultadoAnalisePalpite1.getMotivo(), CoreMatchers.equalTo(MotivoPonto.SALDO_GOLS));
		
		//Cenário Visitante Vence
		palpite = new Palpite();
		palpite.setGolsMandante(2);
		palpite.setGolsVisitante(4);
				
		ResultadoAnalisePalpite resultadoAnalisePalpite2 = analisaSeCravou.acertouNestaCategoria(palpite, partidaVisitanteVence.getResultado());
				
		Assert.assertThat(resultadoAnalisePalpite2.getPontos(), CoreMatchers.is(MotivoPonto.SALDO_GOLS.getPontos()));
		Assert.assertThat(resultadoAnalisePalpite2.getMotivo(), CoreMatchers.equalTo(MotivoPonto.SALDO_GOLS));
	}
	
	@Test
	public void testResultadoSimples() {
		
		//Cenário Mandante Vence
		palpite = new Palpite();
		palpite.setGolsMandante(1);
		palpite.setGolsVisitante(0);
		
		ResultadoAnalisePalpite resultadoAnalisePalpite1 = analisaSeCravou.acertouNestaCategoria(palpite, partidaMandanteVence.getResultado());
		
		Assert.assertThat(resultadoAnalisePalpite1.getPontos(), CoreMatchers.is(MotivoPonto.RESULTADO.getPontos()));
		Assert.assertThat(resultadoAnalisePalpite1.getMotivo(), CoreMatchers.equalTo(MotivoPonto.RESULTADO));
		
		//Cenário Visitante Vence
		palpite = new Palpite();
		palpite.setGolsMandante(0);
		palpite.setGolsVisitante(1);
				
		ResultadoAnalisePalpite resultadoAnalisePalpite2 = analisaSeCravou.acertouNestaCategoria(palpite, partidaVisitanteVence.getResultado());
				
		Assert.assertThat(resultadoAnalisePalpite2.getPontos(), CoreMatchers.is(MotivoPonto.RESULTADO.getPontos()));
		Assert.assertThat(resultadoAnalisePalpite2.getMotivo(), CoreMatchers.equalTo(MotivoPonto.RESULTADO));
	}
	
	@Test
	public void testEmpateGarantido() {
		palpite = new Palpite();
		palpite.setGolsMandante(1);
		palpite.setGolsVisitante(1);
		
		ResultadoAnalisePalpite resultadoAnalisePalpite = analisaSeCravou.acertouNestaCategoria(palpite, partidaMandanteVence.getResultado());
		
		Assert.assertThat(resultadoAnalisePalpite.getPontos(), CoreMatchers.is(MotivoPonto.EMPATE_GARANTIDO.getPontos()));
		Assert.assertThat(resultadoAnalisePalpite.getMotivo(), CoreMatchers.equalTo(MotivoPonto.EMPATE_GARANTIDO));
	}
	
	@Test
	public void testRed() {
		
		//Cenário Mandante Vence
		palpite = new Palpite();
		palpite.setGolsMandante(1);
		palpite.setGolsVisitante(6);
		
		ResultadoAnalisePalpite resultadoAnalisePalpite1 = analisaSeCravou.acertouNestaCategoria(palpite, partidaMandanteVence.getResultado());
		
		Assert.assertThat(resultadoAnalisePalpite1.getPontos(), CoreMatchers.is(0));
		Assert.assertThat(resultadoAnalisePalpite1.getMotivo(), CoreMatchers.nullValue());
		
		//Cenário Visitante Vence
		palpite = new Palpite();
		palpite.setGolsMandante(3);
		palpite.setGolsVisitante(2);
			
		ResultadoAnalisePalpite resultadoAnalisePalpite2 = analisaSeCravou.acertouNestaCategoria(palpite, partidaVisitanteVence.getResultado());
				
		Assert.assertThat(resultadoAnalisePalpite2.getPontos(), CoreMatchers.is(0));
		Assert.assertThat(resultadoAnalisePalpite2.getMotivo(), CoreMatchers.nullValue());
	}

}
