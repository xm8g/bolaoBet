package com.bolao.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.bolao.entity.jogo.Campeonato;
import com.bolao.entity.user.Participante;
import com.bolao.entity.user.Usuario;

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
	
	@ManyToMany
	@JoinTable(name = "boloes_e_participantes", joinColumns = {
			@JoinColumn(name = "bolao_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "participante_id", referencedColumnName = "id") })
	private List<Participante> participantes;
	
	@Size(min=1, message="Você deve enviar pelo menos um convite.")
	@ElementCollection
    @CollectionTable(name = "bolao_convites", joinColumns = @JoinColumn(name = "bolao_id"))
    @Column(name = "convidado")
    private Set<String> convidados = new HashSet<>();
	
	@Transient
	private String nomeCampeonato;
}
