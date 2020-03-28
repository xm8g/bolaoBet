package com.bolao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolao.entity.Bolao;
import com.bolao.entity.jogo.Campeonato;
import com.bolao.entity.jogo.Partida;
import com.bolao.service.CampeonatoService;
import com.bolao.service.PartidaService;
import com.bolao.validator.LocalPartidaValidator;

@Controller
@RequestMapping("/partidas")
public class PartidaController {

	@Autowired
	private PartidaService partidaService;
	
	@Autowired
	private CampeonatoService campeonatoService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new LocalPartidaValidator());
	}
	
	@GetMapping("/nova")
	public String novaPartida(Partida partida, Model model) {
		return "partidas/cadastro";
	}
	
	@GetMapping("/tabela/listagem/{rodada}/{campeonato}")
	public ResponseEntity<?> findPartidasCampeonatoPorRodada(@PathVariable("rodada") Integer rodada, @PathVariable("campeonato") Long campeonato) {

		return ResponseEntity.ok(partidaService.partidasDaRodada(rodada, campeonato));
	}
	
	@GetMapping("/jogos/{rodada}")
	public ResponseEntity<?> findPartidasPorRodada(@PathVariable("rodada") Integer rodada, HttpServletRequest req) {

		Bolao bolao = (Bolao) req.getSession().getAttribute("bolao");
		List<Partida> partidasDaRodada = partidaService.partidasDaRodada(rodada, bolao.getCampeonato().getId());
		if (CollectionUtils.isEmpty(partidasDaRodada)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(partidasDaRodada);
	}
	
	@GetMapping("/resultados")
	public String resultados() {
		return "partidas/resultados";
	}
	
	@ModelAttribute("campeonatos")
	public List<Campeonato> listaCampeonatos() {
		
		return campeonatoService.todos();
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<?> salvar(@Valid Partida partida, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for(FieldError fieldError : result.getFieldErrors()) {
				errors.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		
		partidaService.salvarPartida(partida);
		
		return ResponseEntity.ok().build();	
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> excluiPromocao(@PathVariable("id") Long id) {
		
		partidaService.delete(id);
		
		return ResponseEntity.ok().build();
	}
}
