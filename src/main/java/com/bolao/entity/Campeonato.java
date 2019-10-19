package com.bolao.entity;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "campeonatos")
@NoArgsConstructor
@Getter
@Setter
public class Campeonato extends AbstractEntity {

	@NotBlank(message="O 'Nome' é Campo Obrigatório!")
	private String nome;
	
	@Embedded
	@NotNull(message="O 'Escudo do Time' é Campo Obrigatório!")
	private Escudo escudo;
	
	@NotBlank(message="Informe quantas rodadas ou fases tem esse campeonato.")
	private int rodadas;
	
	// evita recursividade quando o json de resposta for criado para a datatables.
	@JsonIgnore
	@OneToMany(mappedBy = "campeonato")
	private List<Partida> partidas;
}
