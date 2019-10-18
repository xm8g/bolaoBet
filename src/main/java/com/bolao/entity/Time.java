package com.bolao.entity;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

	@Embedded
	@NotNull(message="O 'Escudo do Time' é Campo Obrigatório!")
	private Escudo escudo;
}
