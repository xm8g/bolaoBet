package com.bolao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolao.entity.Bolao;
import com.bolao.entity.dto.Datatables;
import com.bolao.entity.dto.DatatablesColunas;
import com.bolao.repository.BolaoRepository;

@Service
public class BolaoService {

	@Autowired
	private BolaoRepository bolaoRepository;
	
	@Autowired
	private Datatables datatables;
	
	public Bolao buscarPorId(Long id) {
		
		Optional<Bolao> bolao = bolaoRepository.findById(id);
		
		return bolao.orElse(null);
	}

	@Transactional(readOnly = false)
	public void salvarBolao(Bolao bolao) {
		bolaoRepository.save(bolao);
	}
	
	@Transactional(readOnly = true)
	public List<Bolao> boloesDoUsuario(Long id) {
		Optional<List<Bolao>> boloes = bolaoRepository.findByGestorId(id);
		return boloes.orElse(new ArrayList<>());
	}

	@Transactional(readOnly = true)
	public Object todos(HttpServletRequest req) {
		datatables.setRequest(req);
		datatables.setColunas(DatatablesColunas.BOLOES);
		Page<Bolao> page = bolaoRepository.findAll(datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		bolaoRepository.deleteById(id);		
	}
	
	@Transactional(readOnly = true)
	public List<Bolao> boloesConvidados(String usuario, Long id) {
		Optional<List<Bolao>> boloes = bolaoRepository.findBolaoByConvidadoOrCreator(usuario.trim(), id);
		return boloes.orElse(null);
	} 
	
	@Transactional(readOnly = true)
	public List<Bolao> boloesPorCampeonato(Long idCampeonato) {
		Optional<List<Bolao>> boloes = bolaoRepository.findBolaoByCampeonato(idCampeonato);
		return boloes.orElse(null);
	} 
	

}
