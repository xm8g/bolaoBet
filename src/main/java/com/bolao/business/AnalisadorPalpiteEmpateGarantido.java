package com.bolao.business;

import com.bolao.entity.bets.Palpite;
import com.bolao.entity.bets.domain.MotivoPonto;
import com.bolao.entity.jogo.ResultadoPartida;

public class AnalisadorPalpiteEmpateGarantido implements AnalisadorPalpite {

	private AnalisadorPalpite proximo;
	
	@Override
	public void proximaAnalise(AnalisadorPalpite proximo) {
		this.proximo = proximo;
	}

	@Override
	public ResultadoAnalisePalpite acertouNestaCategoria(Palpite palpite, ResultadoPartida resultadoPartida) {
		ResultadoAnalisePalpite resultadoAnalisePalpite = new ResultadoAnalisePalpite();
		
		Integer placarGolsMandante = resultadoPartida.getGolsHTMandante() + resultadoPartida.getGolsFTMandante();
		Integer placarGolsVisitante = resultadoPartida.getGolsHTVisitante() + resultadoPartida.getGolsFTVisitante();
		
		Integer palpiteGolsMandante = palpite.getGolsMandante(); 
		Integer palpiteGolsVisitante = palpite.getGolsVisitante();
		
		if (palpiteGolsMandante == palpiteGolsVisitante && (palpiteGolsMandante != placarGolsMandante || palpiteGolsVisitante != placarGolsVisitante)) {
			resultadoAnalisePalpite.setPontos(MotivoPonto.EMPATE_GARANTIDO.getPontos());
			resultadoAnalisePalpite.setMotivo(MotivoPonto.EMPATE_GARANTIDO);
			
			return resultadoAnalisePalpite;
		}
		
		if (this.proximo == null) {
			resultadoAnalisePalpite.setPontos(MotivoPonto.RED.getPontos());
			resultadoAnalisePalpite.setMotivo(MotivoPonto.RED);
		}
		return resultadoAnalisePalpite;
	}

}
