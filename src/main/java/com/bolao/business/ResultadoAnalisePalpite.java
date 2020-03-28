package com.bolao.business;

import com.bolao.entity.bets.domain.MotivoPonto;

import lombok.Data;

@Data
public class ResultadoAnalisePalpite {

	private Integer pontos;
	
	private MotivoPonto motivo;
}
