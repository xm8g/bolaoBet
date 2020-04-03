package com.bolao.repository.projection;

public interface PontuacaoDetalhada {

	Integer getPontuacaoGeral();
	
	Integer getPontuacaoPorCravadas();
	
	Integer getPontuacaoPorPlacarDoVencedor();
	
	Integer getPontuacaoPorAcertoDeSaldo();
	
	Integer getPontuacaoPorAcertoDeResultado();
	
	Integer getPontuacaoPorEmpateGarantido();
}
