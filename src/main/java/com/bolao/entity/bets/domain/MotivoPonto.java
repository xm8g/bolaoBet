package com.bolao.entity.bets.domain;


public enum MotivoPonto {
	EXATO(18), GOLS_VENCEDOR(13), SALDO_GOLS(11), RESULTADO(9), EMPATE_GARANTIDO(5);
	
	Integer pontos;
	
	MotivoPonto(Integer pontos) {
		this.pontos = pontos;
	}

	public Integer getPontos() {
		return pontos;
	}
	
}
