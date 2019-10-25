package com.bolao.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
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

	@NotBlank(message = "O 'Nome' é Campo Obrigatório!")
	private String nome;

	@Embedded
	@NotNull(message = "O 'Escudo do Time' é Campo Obrigatório!")
	private Escudo escudo;

	@Min(value = 1, message = "O n° de rodadas/fases deve ser maior ou igual a 1.")
	private int rodadas;
	
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "campeonatos_tem_times",
			joinColumns = @JoinColumn(name = "id_campeonato", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_time", referencedColumnName = "id")
    )
	private List<Time> times;

	// evita recursividade quando o json de resposta for criado para a datatables.
	@JsonIgnore
	@OneToMany(mappedBy = "campeonato", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Partida> partidas;
}
