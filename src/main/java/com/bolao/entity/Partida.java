package com.bolao.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "partidas")
@NoArgsConstructor
@Getter
@Setter
public class Partida extends AbstractEntity {

	@NotBlank(message="Selecione a rodada!")
	private int rodada;
	
	@NotBlank(message="Selecione o time mandante!")
	@OneToOne
	@JoinColumn(name = "id_timeA")
	private Time mandante;
	
	@NotBlank(message="Selecione o time visitante!")
	@OneToOne
	@JoinColumn(name = "id_timeB")
	private Time visitante;
	
	@NotNull(message="Entre com a data e hora da partida!")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDate data;

	@NotBlank(message="O local de realização da partida é obrigatório!")
	private String local;
	
	@ManyToOne
	@JoinColumn(name="id_campeonato")
	private Campeonato campeonato;
	
	@Embedded
	private ResultadoPartida resultado;
}
