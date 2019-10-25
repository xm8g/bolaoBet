package com.bolao.entity;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "times")
@NoArgsConstructor
@Getter
@Setter
public class Time extends AbstractEntity {

	@NotBlank(message="O 'Nome' é Campo Obrigatório!")
	private String nome;
	
	@NotBlank(message="O 'País' é Campo Obrigatório!")
	private String pais;
	
	@NotBlank(message="Onde o time sedia seus jogos em casa?")
	private String casa;

	@Embedded
	private Escudo escudo;
	
	@ManyToMany
	@JoinTable(
			name = "campeonatos_tem_times",
			joinColumns = @JoinColumn(name = "id_time", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_campeonato", referencedColumnName = "id")
    )
	private List<Campeonato> campeonato;
}
