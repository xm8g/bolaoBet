package com.bolao.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bolao.entity.jogo.Campeonato;
import com.bolao.entity.jogo.Partida;
import com.bolao.service.CampeonatoService;
import com.bolao.service.PartidaService;
import com.bolao.service.ProcessadorRodada;

@Controller
@RequestMapping("/processador")
public class ProcessadorController {

	@Autowired
	private CampeonatoService campeonatoService;
	
	@Autowired
	private ProcessadorRodada processadorRodada;
	
	@Autowired
	private PartidaService partidaService;
	
	@GetMapping("/rodada")
	public String home() {
		return "processador/rodada";
	}
	
	@ModelAttribute("campeonatos")
	public List<Campeonato> listaCampeonatos() {
		
		return campeonatoService.todos();
	}
	
	@GetMapping("/partidasEncerradas/{rodada}/{campeonato}")
	public ResponseEntity<?> findPartidasEncerradasCampeonatoPorRodada(@PathVariable("rodada") Integer rodada, @PathVariable("campeonato") Long campeonato) {

		List<Partida> partidasEncerradasDaRodada = partidaService.partidasEncerradasDaRodada(rodada, campeonato);
		if (CollectionUtils.isEmpty(partidasEncerradasDaRodada)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidasEncerradasDaRodada);
	}
	
	@PostMapping("/rodada/process")
	public ResponseEntity<?> processarPartidasEncerradas(@RequestBody Partida[] pEncerradas) {
		
		if (pEncerradas != null && pEncerradas.length > 0) {
		    List<Partida> partidasEncerradas = new ArrayList<>(); 
		    CollectionUtils.addAll(partidasEncerradas, pEncerradas);
		    processadorRodada.processarPalpitesPorPartida(partidasEncerradas);
		}
		
		return ResponseEntity.ok().build();
	} 
}
