package com.bolao.entity.bets;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.bolao.entity.AbstractEntity;
import com.bolao.entity.jogo.Partida;
import com.bolao.entity.user.Participante;
import com.bolao.validator.PalpiteNoPrazo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@PalpiteNoPrazo
@Entity
@Table(name = "palpites")
@NoArgsConstructor
@Getter @Setter
public class Palpite extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name="participante_id", referencedColumnName="id")
	private Participante participante;
	
	@ManyToOne
	@JoinColumn(name="partida_id", referencedColumnName="id")
	private Partida partida;
	
	@Min(value=0)
	private Integer golsMandante;
	
	@Min(value=0)
	private Integer golsVisitante;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Column(name="data_hora_palpite",  columnDefinition = "TIMESTAMP")
	private LocalDateTime dataDoPalpite;
	
	private Boolean valido;
	
	private Boolean processado;
	
	@Embedded
	private Pontuacao pontosGanhos;
}
