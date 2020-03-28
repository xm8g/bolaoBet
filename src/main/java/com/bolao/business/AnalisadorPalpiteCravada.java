package com.bolao.business;

import com.bolao.entity.bets.Palpite;
import com.bolao.entity.bets.domain.MotivoPonto;
import com.bolao.entity.jogo.ResultadoPartida;

public class AnalisadorPalpiteCravada implements AnalisadorPalpite {

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
		
		if (placarGolsMandante == palpiteGolsMandante && placarGolsVisitante == palpiteGolsVisitante) {
			ResultadoAnalisePalpite resultadoAnalisePalpite = new ResultadoAnalisePalpite();
			resultadoAnalisePalpite.setPontos(MotivoPonto.EXATO.getPontos());
			resultadoAnalisePalpite.setMotivo(MotivoPonto.EXATO);
			
			return resultadoAnalisePalpite;
		}
		return proximo.acertouNestaCategoria(palpite, resultadoPartida);
	}

}
