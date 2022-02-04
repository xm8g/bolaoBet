package com.bolao.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolao.entity.Bolao;
import com.bolao.entity.jogo.Partida;
import com.bolao.entity.user.Participante;
import com.bolao.form.RodadaClassificacao;
import com.bolao.repository.projection.ClassificacaoChart;
import com.bolao.repository.projection.ClassificacaoLista;
import com.bolao.service.BolaoService;
import com.bolao.service.ClassificacaoService;
import com.bolao.service.PalpiteService;
import com.bolao.service.ParticipanteService;
import com.bolao.service.PartidaService;

@Controller
@RequestMapping("/classificacao")
public class ClassificacaoController {

	@Autowired
	private ClassificacaoService classificacaoService;
	
	@Autowired
	private PartidaService partidaService;
	
	@Autowired
	private ParticipanteService participanteService;
	
	@Autowired
	private PalpiteService palpiteService;
	
	@Autowired
	private BolaoService bolaoService;
	
	
	@GetMapping("/home")
	public String home(Model model, HttpServletRequest req, @AuthenticationPrincipal User user) {
		
		
		model.addAttribute("rodadaClassificacao", new RodadaClassificacao());
		Bolao bolao = (Bolao) req.getSession().getAttribute("bolao");
		
		List<Integer> rodadasDoBolaoQueJaForamClassificadas = classificacaoService.buscaRodadasDoBolaoQueJaForamClassificadas(bolao.getId());
		
		model.addAttribute("rodadas", rodadasDoBolaoQueJaForamClassificadas);
		
		List<ClassificacaoLista> classificacao = classificacaoService.obterClassificacao(bolao, user, 0);
		if (CollectionUtils.isEmpty(classificacao)) {
			model.addAttribute("classificacaoGeral", new ArrayList<ClassificacaoLista>());
		} else {
			Comparator<ClassificacaoLista> classificacaoPorPontosComparator = Comparator.comparing(ClassificacaoLista::getPontos);
		     
		    Collections.sort(classificacao, classificacaoPorPontosComparator.reversed());
			
			model.addAttribute("classificacaoGeral", classificacao);
			
			List<ClassificacaoChart> historicoDeClassificacoesPorParticipante = new ArrayList<>();
			
			criaListaComOsParticipantes(historicoDeClassificacoesPorParticipante, bolao.getId());
			
			List<ClassificacaoLista> historicoEvolucao = new ArrayList<>();
			for(Integer rodada : rodadasDoBolaoQueJaForamClassificadas) {
				List<ClassificacaoLista> classificacaoRodada = classificacaoService.obterClassificacao(bolao, user, rodada);
				somarComClassificacaoAnterior(historicoEvolucao, classificacaoRodada);
				Collections.sort(historicoEvolucao, classificacaoPorPontosComparator.reversed());
				
				int ranking = 1;
				for (ClassificacaoLista c : historicoEvolucao) {
					colocarNoHistoricoAPosicaoDesteParticipante(historicoDeClassificacoesPorParticipante, c.getNomeParticipante(), ranking);
					ranking++;
				}
				
				model.addAttribute("historico", historicoDeClassificacoesPorParticipante);
			}
		}
		return "bolao/classificacao";
	}
	
	private void somarComClassificacaoAnterior(List<ClassificacaoLista> historicoEvolucao,
			List<ClassificacaoLista> classificacaoRodada) {
		if (historicoEvolucao.isEmpty()) {
			historicoEvolucao.addAll(classificacaoRodada); 
			return;
		}
		List<ClassificacaoLista> novaClassificacaoSomada = new ArrayList<>();
		for(ClassificacaoLista h : historicoEvolucao)
			for (ClassificacaoLista classificacaoLista : classificacaoRodada) {
				if (h.getNomeParticipante().equals(classificacaoLista.getNomeParticipante())) {
					ClassificacaoLista c = new ClassificacaoLista();
					c.setNomeParticipante(classificacaoLista.getNomeParticipante());
					c.setPontos(h.getPontos() + classificacaoLista.getPontos());
					novaClassificacaoSomada.add(c);
				}
			}
		historicoEvolucao.clear();
		historicoEvolucao.addAll(novaClassificacaoSomada);
		
	}

	private void colocarNoHistoricoAPosicaoDesteParticipante(
			List<ClassificacaoChart> historicoDeClassificacoesPorParticipante, String nomeParticipante, int ranking) {
		for (ClassificacaoChart classificacaoChart : historicoDeClassificacoesPorParticipante) {
			if (nomeParticipante.equals(classificacaoChart.getNome())) {
				classificacaoChart.addRanking(ranking);
			}
		}
		
	}
	
	private void criaListaComOsParticipantes(List<ClassificacaoChart> historicoDeClassificacoesPorParticipante, Long idBolao) {
		List<Participante> participantesDoBolao = participanteService.participantesDoBolao(idBolao);
		for (Participante participante : participantesDoBolao) {
			ClassificacaoChart classificacaoChart = new ClassificacaoChart();
			classificacaoChart.setNome(participante.getUsuario().getEmail());
			historicoDeClassificacoesPorParticipante.add(classificacaoChart);
		}
	}
	

	@PostMapping("/tipo")
	public String classificacaoPorRodada(RodadaClassificacao rodadaClassificacao, Model model, HttpServletRequest req, @AuthenticationPrincipal User user) {
		
		Bolao bolao = (Bolao) req.getSession().getAttribute("bolao");
		model.addAttribute("rodadas", classificacaoService.buscaRodadasDoBolaoQueJaForamClassificadas(bolao.getId()));
		
		
		if (rodadaClassificacao.getRodada() == 0) {
			return "redirect:/classificacao/home";
		} else {
			List<ClassificacaoLista> classificacao = classificacaoService.obterClassificacao(bolao, user, rodadaClassificacao.getRodada());
			if (CollectionUtils.isEmpty(classificacao)) {
				model.addAttribute("classificacaoGeral", new ArrayList<ClassificacaoLista>());
			} else {
				Comparator<ClassificacaoLista> classificacaoPorPontosComparator = Comparator.comparing(ClassificacaoLista::getPontos);
			     
			    Collections.sort(classificacao, classificacaoPorPontosComparator.reversed());
				
				model.addAttribute("classificacaoGeral", classificacao);	
			}	
		}
		return "bolao/classificacao";
	}
	
	@GetMapping("/rollback/{rodada}/{campeonato}")
	public String voltarProcessamentoDaRodada(@PathVariable("rodada") Integer rodada,  @PathVariable("campeonato") Long campeonatoId, RedirectAttributes attr) {
		
		List<Bolao> boloesComEsteCampeonato = bolaoService.boloesPorCampeonato(campeonatoId);
		if (CollectionUtils.isNotEmpty(boloesComEsteCampeonato)) {
			for (Bolao bolao : boloesComEsteCampeonato) {
				classificacaoService.desclassificarRodadaDoBolao(rodada, bolao.getId());	
			}
		}
		
		List<Partida> partidasEncerradasDaRodada = partidaService.partidasEncerradasDaRodada(rodada, campeonatoId);
		if (CollectionUtils.isNotEmpty(partidasEncerradasDaRodada)) {
			for (Partida partida : partidasEncerradasDaRodada) {
				palpiteService.rollbackPalpitesPontuadosProcessadosPorPartida(partida.getId());
			}
			
		}
		
		attr.addFlashAttribute("rodada", rodada);
		attr.addFlashAttribute("campeonatoId", campeonatoId);
		
		return "redirect:/processador/rodada";
	}
	
}
