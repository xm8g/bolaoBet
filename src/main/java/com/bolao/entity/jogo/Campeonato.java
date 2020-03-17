package com.bolao.entity.jogo;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import com.bolao.entity.AbstractEntity;
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
	
	@NotBlank(message="O 'País' é Campo Obrigatório!")
	private String pais;

	@Embedded
	private Escudo escudo;

	@Min(value = 1, message = "O n° de rodadas/fases deve ser maior ou igual a 1.")
	private int rodadas;
	
	@Min(value = 2, message = "Informe o número de times que participarão.")
	private int qtdeTimes;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "campeonatos_tem_times",
			joinColumns = @JoinColumn(name = "id_campeonato", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_time", referencedColumnName = "id")
    )
	private Set<Time> times;

	// evita recursividade quando o json de resposta for criado para a datatables.
	@JsonIgnore
	@OneToMany(mappedBy = "campeonato", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Partida> partidas;
	
	@Transient
	private String logo;
	
	public String getLogo() {
		if (escudo != null) {
			return Base64.encodeBase64String(escudo.getData()); 
		}
		return StringUtils.EMPTY;
	}
}
