package com.bolao.entity.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.bolao.entity.AbstractEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter @NoArgsConstructor
@Entity
public class Participante extends AbstractEntity {

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	private Integer pontuacaoGeral;
	
	private Integer pontuacaoPorCravadas;
	
	private Integer pontuacaoPorPlacarDoVencedor;
	
	private Integer pontuacaoPorAcertoDeSaldo;
	
	private Integer pontuacaoPorAcertoDeResultado;
	
	private Integer pontuacaoPorAcertoDeGolsDoPerdedor;
	
	private Integer pontuacaoPorEmpateGarantido;
	
}

