package com.bolao.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Embeddable
public class ResultadoPartida {

	@Column(name="gols_ht_mandante")
	private int golsHTMandante = -1;
	
	@Column(name="gols_ht_visitante")
	private int golsHTVisitante = -1;
	
	@Column(name="gols_ft_mandante")
	private int golsFTMandante = -1;
	
	@Column(name="gols_ft_visitante")
	private int golsFTVisitante = -1;

	
}
