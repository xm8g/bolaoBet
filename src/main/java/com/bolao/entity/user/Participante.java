package com.bolao.entity.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import com.bolao.entity.AbstractEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter @NoArgsConstructor
@Entity
public class Participante extends AbstractEntity {

	@NotBlank(message="Apelido não pode ser vazio")
	private String apelido;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
}
