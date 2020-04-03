package com.bolao.repository.projection;

import lombok.Data;

@Data
public class ClassificacaoLista {

	String  avatar;
	String  nomeParticipante;
	Integer pontos;
	Integer pontuacaoPorCravadas;
	Integer pontuacaoPorPlacarDoVencedor;
	Integer pontuacaoPorAcertoDeSaldo;
	Integer pontuacaoPorAcertoDeResultado;
	Integer pontuacaoPorEmpateGarantido;
	boolean currentUser;
	
}
