package com.bolao.entity;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class ResultadoPartida {

	private int golsHTMandante;
	
	private int golsHTVisitante;
	
	private int golsFTMandante;
	
	private int golsFTVisitante;
	
	
}
