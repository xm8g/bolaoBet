package com.bolao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolao.entity.user.Participante;
import com.bolao.repository.ParticipanteRepositoy;

@Service
public class ParticipanteService {

	@Autowired
	private ParticipanteRepositoy participanteRepositoy;
	
	@Transactional(readOnly = true)
	public List<Participante> participantesDoBolao(Long idBolao) {
		Optional<List<Participante>> participantes = participanteRepositoy.findParticipanteByBolao(idBolao);
		return participantes.orElse(new ArrayList<>());
	} 
}
