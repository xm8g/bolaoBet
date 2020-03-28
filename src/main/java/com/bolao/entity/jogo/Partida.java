package com.bolao.entity.jogo;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.bolao.entity.AbstractEntity;
import com.bolao.entity.bets.Palpite;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@Table(name = "partidas")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Partida extends AbstractEntity {

	@NotNull(message="Selecione a rodada!")
	@Min(1)
	private int rodada;
	
	@NotNull(message="Selecione o time mandante!")
	@OneToOne
	@JoinColumn(name = "time_a")
	private Time mandante;
	
	@NotNull(message="Selecione o time visitante!")
	@OneToOne
	@JoinColumn(name = "time_b")
	private Time visitante;
	
	@NotNull(message="Entre com a data e hora da partida!")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Column(name="data_hora",  columnDefinition = "TIMESTAMP")
	private LocalDateTime data;

	@NotBlank(message="O local de realização da partida é obrigatório!")
	private String local;
	
	@ManyToOne
	@JoinColumn(name="id_campeonato")
	private Campeonato campeonato;
	
	@JsonIgnore
	@OneToMany(mappedBy="partida", cascade=CascadeType.REMOVE)
	private List<Palpite> palpites;
	
	@Embedded
	private ResultadoPartida resultado;
	
	private boolean encerrada;
	
	
	
}
