package com.bolao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolao.entity.jogo.Partida;
import com.bolao.entity.jogo.ResultadoPartida;
import com.bolao.repository.PartidaRepository;

@Service
public class PartidaService {

	@Autowired
	private PartidaRepository partidaRepository;
	
	public void salvarPartida(Partida partida) {
		partidaRepository.save(partida);
	}

	@Transactional(readOnly = true)
	public List<Partida> partidasDaRodada(Integer rodada) {
		Optional<List<Partida>> partidas = partidaRepository.findByRodada(rodada);
		return partidas.orElse(new ArrayList<>());
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		partidaRepository.deleteById(id);		
	}
	
	@Transactional(readOnly = false)
	public void editarPlacarFinal(Long id, ResultadoPartida resultadoPartida) {
		Partida partida = partidaRepository.findById(id).get();
		partida.setResultado(resultadoPartida);
	}
}
