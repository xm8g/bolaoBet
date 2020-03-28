package com.bolao.business;

import com.bolao.entity.bets.Palpite;
import com.bolao.entity.bets.domain.MotivoPonto;
import com.bolao.entity.jogo.ResultadoPartida;

public class AnalisadorPalpiteSaldoGols implements AnalisadorPalpite {

private AnalisadorPalpite proximo;
	
	@Override
	public void proximaAnalise(AnalisadorPalpite proximo) {
		this.proximo = proximo;
	}

	@Override
	public ResultadoAnalisePalpite acertouNestaCategoria(Palpite palpite, ResultadoPartida resultadoPartida) {
		Integer placarGolsMandante = resultadoPartida.getGolsHTMandante() + resultadoPartida.getGolsFTMandante();
		Integer placarGolsVisitante = resultadoPartida.getGolsHTVisitante() + resultadoPartida.getGolsFTVisitante();
		
		Integer palpiteGolsMandante = palpite.getGolsMandante(); 
		Integer palpiteGolsVisitante = palpite.getGolsVisitante();
		
		Integer saldoPlacar = placarGolsMandante - placarGolsVisitante;
		Integer saldoPalpite = palpiteGolsMandante - palpiteGolsVisitante;
		
		if (saldoPlacar == 0) { //houve empate. Nada a fazer
			return proximo.acertouNestaCategoria(palpite, resultadoPartida);
		} else {
			if (saldoPlacar == saldoPalpite) {
				ResultadoAnalisePalpite resultadoAnalisePalpite = new ResultadoAnalisePalpite();
				resultadoAnalisePalpite.setPontos(MotivoPonto.SALDO_GOLS.getPontos());
				resultadoAnalisePalpite.setMotivo(MotivoPonto.SALDO_GOLS);
				
				return resultadoAnalisePalpite;
			}
		}
		return proximo.acertouNestaCategoria(palpite, resultadoPartida);
	}

}
