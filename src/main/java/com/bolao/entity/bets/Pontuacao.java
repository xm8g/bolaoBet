package com.bolao.entity.bets;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.bolao.entity.bets.domain.MotivoPonto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Embeddable
public class Pontuacao {

	private Integer pontos;
	
	private Integer coins;
	
	@Enumerated(EnumType.STRING)
	private MotivoPonto motivoPonto;
	
	
}
