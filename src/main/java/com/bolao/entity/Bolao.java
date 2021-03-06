package com.bolao.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.apache.commons.collections4.CollectionUtils;

import com.bolao.entity.jogo.Campeonato;
import com.bolao.entity.user.Participante;
import com.bolao.entity.user.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "boloes")
public class Bolao extends AbstractEntity {

	@NotBlank(message="Digite o nome do bolão!")
	private String nome;
	
	@ManyToOne
	@JoinColumn(name="campeonato_id", referencedColumnName="id")
	private Campeonato campeonato;
	
	@ManyToOne
	@JoinColumn(name="usuario_id", referencedColumnName="id")
	private Usuario gestor;
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(name = "boloes_e_participantes", joinColumns = {
			@JoinColumn(name = "bolao_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "participante_id", referencedColumnName = "id") })
	private Set<Participante> participantes;
	
	@ElementCollection
    @CollectionTable(name = "bolao_convites", joinColumns = @JoinColumn(name = "bolao_id", referencedColumnName = "id"))
    @Column(name = "convidado")
    private List<String> convidados = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "bolao", fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<ClassificacaoRodada> classificacao;
	
	@Transient
	private String nomeCampeonato;
	
	public void addParticipante(Participante p) {
		if (CollectionUtils.isEmpty(participantes)) {
			participantes = new HashSet<Participante>();
		}
		participantes.add(p);
	}
}
