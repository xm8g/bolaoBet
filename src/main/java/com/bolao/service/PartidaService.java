package com.bolao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolao.entity.Partida;
import com.bolao.repository.PartidaRepository;

@Service
public class PartidaService {

	@Autowired
	private PartidaRepository partidaRepository;

	public void salvarPartida(Partida partida) {
		partidaRepository.save(partida);
	}
}
