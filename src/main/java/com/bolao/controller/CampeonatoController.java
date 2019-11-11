package com.bolao.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolao.entity.Campeonato;
import com.bolao.service.CampeonatoService;

@Controller
@RequestMapping("/campeonatos")
public class CampeonatoController {

	@Autowired
	private CampeonatoService campeonatoService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new CampeonatoValidator());
	}
	
	@GetMapping("/novo")
	public String novoCampeonato(Campeonato campeonato, Model model) {
		return "campeonatos/cadastro";
	}
	
	@GetMapping("/listagem")
	public String listagem() {
		return "campeonatos/listagem";
	}
	
	@GetMapping("/tabela/listagem")
	public ResponseEntity<?> findCampeonatos(HttpServletRequest req) {
		return ResponseEntity.ok(campeonatoService.todos(req));
	}
	
	@PostMapping("/salvar")
	public String salvar(@RequestParam("escudo") MultipartFile file, @Valid Campeonato campeonato, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "campeonatos/cadastro";
		}
		campeonatoService.salvarCampeonato(campeonato);
		attr.addFlashAttribute("sucesso", "ok");
		return "redirect:/campeonatos/novo";	
	}
	
	@GetMapping("/editar/{id}")
	public String preEditarCampeonato(@PathVariable("id") Long id, ModelMap model) {
		
		Campeonato campeonato = campeonatoService.buscarCampeonato(id);
		model.addAttribute("campeonato", campeonato);
		
		return "campeonatos/cadastro";
	}
	
	@GetMapping("/numeroDeTimes")
	public ResponseEntity<?> getQtdeTimes(@RequestParam("idCampeonato") String idCampeonato ) {
		
		Campeonato campeonato = campeonatoService.buscarCampeonato(Long.valueOf(idCampeonato));
		if (CollectionUtils.isNotEmpty(campeonato.getTimes())) {
			return ResponseEntity.ok(campeonato.getTimes().size());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/editar")
	public String editar(@RequestParam("escudo") MultipartFile file, @Valid Campeonato campeonato, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "campeonatos/cadastro";
		}
		campeonatoService.editar(campeonato);
		model.addAttribute("sucesso", "ok");
		
		return "campeonatos/cadastro";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirTime(@PathVariable("id") Long id,  RedirectAttributes attr) {
		campeonatoService.remover(id);
		
		return "redirect:/campeonatos/listagem";
	}
}
