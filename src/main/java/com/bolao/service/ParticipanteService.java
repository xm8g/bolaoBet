package com.bolao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolao.entity.user.Participante;
import com.bolao.repository.ParticipanteRepositoy;
import com.bolao.repository.projection.PontuacaoDetalhada;

@Service
public class ParticipanteService {

	@Autowired
	private ParticipanteRepositoy participanteRepositoy;
	
	@Transactional(readOnly = true)
	public List<Participante> participantesDoBolao(Long idBolao) {
		Optional<List<Participante>> participantes = participanteRepositoy.findParticipanteByBolao(idBolao);
		return participantes.orElse(new ArrayList<>());
	}
	
	@Transactional(readOnly = false)
	public void atualizarPontuacaoDetalhadaDoParticipante(Long idParticipante, PontuacaoDetalhada pontuacao) {
		Optional<Participante> optionalParticipante = participanteRepositoy.findById(idParticipante);
		
		Participante participante = optionalParticipante.orElse(null);
		if (participante != null) {
			participante.somarPontuacaoGeral(pontuacao.getPontuacaoGeral());
			participante.somarPontuacaoPorCravadas(pontuacao.getPontuacaoPorCravadas());
			participante.somarPontuacaoPorPlacarDoVencedors(pontuacao.getPontuacaoPorPlacarDoVencedor());
			participante.somarPontuacaoPorAcertoDeSaldo(pontuacao.getPontuacaoPorAcertoDeSaldo());
			participante.somarPontuacaoPorAcertoDeResultado(pontuacao.getPontuacaoPorAcertoDeResultado());
			participante.somarPontuacaoPorEmpateGarantido(pontuacao.getPontuacaoPorEmpateGarantido());
		}
	}
}
