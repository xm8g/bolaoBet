package com.bolao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import com.bolao.entity.Bolao;
import com.bolao.entity.ClassificacaoRodada;
import com.bolao.entity.jogo.Campeonato;
import com.bolao.entity.jogo.Partida;
import com.bolao.entity.user.Participante;
import com.bolao.repository.projection.PontuacaoDetalhada;
import com.bolao.service.BolaoService;
import com.bolao.service.CampeonatoService;
import com.bolao.service.ClassificacaoService;
import com.bolao.service.PalpiteService;
import com.bolao.service.ParticipanteService;
import com.bolao.service.PartidaService;
import com.bolao.service.ProcessadorRodada;

@Controller
@RequestMapping("/processador")
public class ProcessadorController {

	@Autowired
	private CampeonatoService campeonatoService;
	
	@Autowired
	private BolaoService bolaoService;
	
	@Autowired
	private PalpiteService palpiteService;
	
	@Autowired
	private ParticipanteService participanteService;
	
	@Autowired
	private ProcessadorRodada processadorRodada;
	
	@Autowired
	private PartidaService partidaService;
	
	@Autowired
	private ClassificacaoService classificacaoService;
	
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
	
	/**
	 * 1- obter os boloes cujo campeonato seja este.
	 * 2- obter os participantes do bolão
	 * 3- obter a pontuacao dos palpites não processados do participante
	 * 4- somar os pontos dos palpites
	 * 5- gravar na tabela de participantes os detalhes dos pontos
	 * 6- gravar na tabela de classificacao um registro de pontos de cada particpante na rodada
	 * 7 -gravar que o palpite foi processado
	 */
	@GetMapping("/classificacao/process/{campeonato}/{rodada}")
	public ResponseEntity<?> processarClassificacao(@PathVariable("campeonato") Long campeonato, @PathVariable("rodada") Integer rodada) {

		List<Bolao> boloesComEsteCampeonato = bolaoService.boloesPorCampeonato(campeonato);
		for (Bolao bolao : boloesComEsteCampeonato) {
			Set<Participante> participantes = bolao.getParticipantes();
			if (CollectionUtils.isNotEmpty(participantes)) {
				for (Participante participante : participantes) {
					PontuacaoDetalhada pontuacaoDetalhadaPorParticipante = palpiteService.buscarBalancoFinalDosPalpitesDoPartipanteNaRodada(participante.getId());
					participanteService.atualizarPontuacaoDetalhadaDoParticipante(participante.getId(), pontuacaoDetalhadaPorParticipante);
					ClassificacaoRodada c = geraClassificacaoRodadaDoParticipante(rodada, bolao, participante, pontuacaoDetalhadaPorParticipante);
					classificacaoService.salvarClassificacaoDaRodada(c);
					palpiteService.encerrarPalpitesPontuadosNaoProcessadosDoParticipante(participante.getId());
				}
			}
		}
		
	
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/isClassificacaoEncerrada/{rodada}/{campeonato}")
	public ResponseEntity<?> classificacaoAindaPodeSerFeita(@PathVariable("rodada") Integer rodada, @PathVariable("campeonato") Long campeonato) {
		
		List<Bolao> boloesComEsteCampeonato = bolaoService.boloesPorCampeonato(campeonato);
		if (CollectionUtils.isEmpty(boloesComEsteCampeonato)) {
			return ResponseEntity.unprocessableEntity().build();
		}
		boolean achouBolaoProcessado = false;
		for (Bolao bolao : boloesComEsteCampeonato) {
			if (classificacaoService.classificacaoJaFoiProcessada(bolao.getId(), rodada)) {
				achouBolaoProcessado = true;
			}
		}		
		if (achouBolaoProcessado) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build(); 
	}

	private ClassificacaoRodada geraClassificacaoRodadaDoParticipante(Integer rodada, Bolao bolao, Participante participante,
			PontuacaoDetalhada pontuacaoDetalhadaPorParticipante) {
		ClassificacaoRodada classificacaoRodada = new ClassificacaoRodada();
		classificacaoRodada.setBolao(bolao);
		classificacaoRodada.setRodada(rodada);
		classificacaoRodada.setParticipante(participante);
		classificacaoRodada.setPontos(pontuacaoDetalhadaPorParticipante.getPontuacaoGeral());
		
		return classificacaoRodada;
	}
}
