package com.bolao.business;

import com.bolao.entity.bets.Palpite;
import com.bolao.entity.bets.domain.MotivoPonto;
import com.bolao.entity.jogo.ResultadoPartida;

public class AnalisadorPalpiteResultadoSimples implements AnalisadorPalpite {

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
		
		if ((placarGolsMandante > placarGolsVisitante && palpiteGolsMandante > palpiteGolsVisitante) ||
				(placarGolsMandante < placarGolsVisitante && palpiteGolsMandante < palpiteGolsVisitante) ||
				(placarGolsMandante == placarGolsVisitante && palpiteGolsMandante == palpiteGolsVisitante)) {
			
			ResultadoAnalisePalpite resultadoAnalisePalpite = new ResultadoAnalisePalpite();
			resultadoAnalisePalpite.setPontos(MotivoPonto.RESULTADO.getPontos());
			resultadoAnalisePalpite.setMotivo(MotivoPonto.RESULTADO);
			
			return resultadoAnalisePalpite;
		}
		
		return proximo.acertouNestaCategoria(palpite, resultadoPartida);
	}
}
