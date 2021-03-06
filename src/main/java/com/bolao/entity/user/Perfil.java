package com.bolao.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bolao.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "perfis")
@Getter @Setter
public class Perfil extends AbstractEntity {
	
	@Column(name = "descricao", nullable = false, unique = true)
	private String desc;
	
	public Perfil() {
		super();
	}

	public Perfil(Long id) {
		super.setId(id);
	}
}
