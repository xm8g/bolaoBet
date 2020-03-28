package com.bolao.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.bolao.entity.user.Participante;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter @Setter
public class ClassificacaoRodada extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name="id_bolao")
	private Bolao bolao;
	
	@NotNull
	private Integer rodada;
	
	@OneToOne
	@JoinColumn(name = "id_participante")
	private Participante participante;
	
	@NotNull
	private Integer pontos;
	
	
}
