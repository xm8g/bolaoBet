package com.bolao.business;

import com.bolao.entity.bets.Palpite;
import com.bolao.entity.bets.domain.MotivoPonto;
import com.bolao.entity.jogo.ResultadoPartida;

public class AnalisadorPalpiteGolsVencedor implements AnalisadorPalpite {

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
		
		Integer golsDoVencedor = -1;
		Integer palpiteGolsVencedor = -1;
		
		if (placarGolsMandante == placarGolsVisitante || palpiteGolsMandante == palpiteGolsVisitante) { //sem análise em caso de empate
			return proximo.acertouNestaCategoria(palpite, resultadoPartida);
		}
		
		if (placarGolsMandante > placarGolsVisitante) {
			golsDoVencedor = placarGolsMandante;	
		} else if (placarGolsMandante < placarGolsVisitante) {
			golsDoVencedor = placarGolsVisitante;	
		}
		if (palpiteGolsMandante > palpiteGolsVisitante) {
			palpiteGolsVencedor = palpiteGolsMandante;	
		} else if (palpiteGolsMandante < palpiteGolsVisitante) {
			palpiteGolsVencedor = palpiteGolsVisitante;	
		}
		
		
		if (golsDoVencedor > -1 && palpiteGolsVencedor > -1 && (golsDoVencedor == palpiteGolsVencedor)) {
			ResultadoAnalisePalpite resultadoAnalisePalpite = new ResultadoAnalisePalpite();
			resultadoAnalisePalpite.setPontos(MotivoPonto.GOLS_VENCEDOR.getPontos());
			resultadoAnalisePalpite.setMotivo(MotivoPonto.GOLS_VENCEDOR);
			
			return resultadoAnalisePalpite;
		}
		return proximo.acertouNestaCategoria(palpite, resultadoPartida);
	}

}
