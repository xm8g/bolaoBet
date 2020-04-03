package com.bolao.business;

import com.bolao.entity.bets.Palpite;
import com.bolao.entity.jogo.ResultadoPartida;

public interface AnalisadorPalpite {

	public void proximaAnalise(AnalisadorPalpite proximo);
	
	public ResultadoAnalisePalpite acertouNestaCategoria(Palpite palpite, ResultadoPartida resultadoPartida);
}
