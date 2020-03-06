package com.bolao.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolao.entity.Bolao;
import com.bolao.repository.BolaoRepository;

@Service
public class BolaoService {

	@Autowired
	private BolaoRepository bolaoRepository;
	
	public Bolao buscarPorId(Long id) {
		
		Optional<Bolao> bolao = bolaoRepository.findById(id);
		
		return bolao.orElse(null);
	}

	public void salvarBolao(Bolao bolao) {
		bolaoRepository.save(bolao);
	}

}
