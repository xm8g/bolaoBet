package com.bolao.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolao.entity.dto.Datatables;
import com.bolao.entity.dto.DatatablesColunas;
import com.bolao.entity.jogo.Campeonato;
import com.bolao.entity.jogo.Time;
import com.bolao.repository.CampeonatoRepository;

@Service
public class CampeonatoService {

	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvarCampeonato(Campeonato campeonato) {
		campeonatoRepository.save(campeonato);
	}

	@Transactional(readOnly = true)
	public Object todos(HttpServletRequest req) {
		datatables.setRequest(req);
		datatables.setColunas(DatatablesColunas.CAMPEONATOS);
		Page<Campeonato> page = campeonatoRepository.findAll(datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void editar(Campeonato campeonato) {
		Campeonato c2 = campeonatoRepository.findById(campeonato.getId()).get();
		c2.setNome(campeonato.getNome());
		c2.setRodadas(campeonato.getRodadas());
		c2.getTimes().addAll(campeonato.getTimes());
		if (campeonato.getEscudo() != null) {
			c2.setEscudo(campeonato.getEscudo());
		}
	}

	@Transactional(readOnly = false)
	public Campeonato buscarCampeonato(Long id) {
		return campeonatoRepository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	public Set<Time> buscarTimes(Long id) {
		Campeonato campeonato = buscarCampeonato(id);
		if (campeonato != null) {
			return campeonato.getTimes();
		}
		return new HashSet<>();
	}

	public List<Campeonato> todos() {
		return campeonatoRepository.findAll();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		campeonatoRepository.deleteById(id);		
	}
}
