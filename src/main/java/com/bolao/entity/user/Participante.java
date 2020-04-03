package com.bolao.entity.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.bolao.entity.AbstractEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter @NoArgsConstructor
@Entity
public class Participante extends AbstractEntity {

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	private Integer pontuacaoGeral;
	
	private Integer pontuacaoPorCravadas;
	
	private Integer pontuacaoPorPlacarDoVencedor;
	
	private Integer pontuacaoPorAcertoDeSaldo;
	
	private Integer pontuacaoPorAcertoDeResultado;
	
	private Integer pontuacaoPorEmpateGarantido;

	public void somarPontuacaoGeral(Integer addPontos) {
		setPontuacaoGeral((getPontuacaoGeral() != null ? getPontuacaoGeral() : 0) + addPontos);
	}

	public void somarPontuacaoPorCravadas(Integer addPontos) {
		setPontuacaoPorCravadas((getPontuacaoPorCravadas() != null ? getPontuacaoPorCravadas() : 0) + addPontos);
	}
	
	public void somarPontuacaoPorPlacarDoVencedors(Integer addPontos) {
		setPontuacaoPorPlacarDoVencedor((getPontuacaoPorPlacarDoVencedor() != null ? getPontuacaoPorPlacarDoVencedor() : 0) + addPontos);
	}
	
	public void somarPontuacaoPorAcertoDeSaldo(Integer addPontos) {
		setPontuacaoPorAcertoDeSaldo((getPontuacaoPorAcertoDeSaldo() != null ? getPontuacaoPorAcertoDeSaldo() : 0) + addPontos);
	}
	
	public void somarPontuacaoPorAcertoDeResultado(Integer addPontos) {
		setPontuacaoPorAcertoDeResultado((getPontuacaoPorAcertoDeResultado() != null ? getPontuacaoPorAcertoDeResultado() : 0) + addPontos);
	}
	
	public void somarPontuacaoPorEmpateGarantido(Integer addPontos) {
		setPontuacaoPorEmpateGarantido((getPontuacaoPorEmpateGarantido() != null ? getPontuacaoPorEmpateGarantido() : 0) + addPontos); 
	}
}

