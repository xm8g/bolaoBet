package com.bolao.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolao.entity.bets.Palpite;
import com.bolao.entity.jogo.Partida;
import com.bolao.repository.PalpiteRepository;

@Service
public class PalpiteService {

	@Autowired
	private PalpiteRepository palpiteRepository;
	
	@Transactional(readOnly = true)
	public Palpite buscarPalpiteDoUsuarioDaPartida(Long idPartida, Long idParticipante) {
		Optional<Palpite> palpite = palpiteRepository.findByPartidaAndParticipante(idPartida, idParticipante);
		return palpite.orElse(null);
	}
	
	@Transactional(readOnly = false)
	public void salvarPalpite(Palpite p) {
		p.setDataDoPalpite(LocalDateTime.now());
		p.setValido(true);
		p.setProcessado(false);
		palpiteRepository.save(p);
	}
	
	@Transactional(readOnly = false)
	public void atualizarPalpitePorId(Long idPalpite, Palpite p) {
		Optional<Palpite> optionalPalpite = palpiteRepository.findById(idPalpite);
		
		Palpite palpite = optionalPalpite.orElse(null);
		if (palpite != null) {
			palpite.setDataDoPalpite(LocalDateTime.now());
			palpite.setValido(true);
			palpite.setProcessado(false);
			palpite.setGolsMandante(p.getGolsMandante());
			palpite.setGolsVisitante(p.getGolsVisitante());
		}
	}
	
	@Transactional(readOnly = true)
	public List<Palpite> palpitesDaPartidaParaSeremProcessados(Long idPartida) {
		Optional<List<Palpite>> palpites = palpiteRepository.findPalpitesNaoProcessadosByPartida(idPartida);
		return palpites.orElse(new ArrayList<>());
	}
}