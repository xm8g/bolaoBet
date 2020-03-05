package com.bolao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolao.entity.domain.Paises;
import com.bolao.entity.jogo.Time;
import com.bolao.service.TimeService;

@Controller
@RequestMapping("times") 
public class TimesController {

	@Autowired
	private TimeService timeService;
	
	@GetMapping("/novoTime")
	public String novoTime(Time time, Model model) {
		model.addAttribute("paises", Paises.values());
		return "times/cadastro";
	}
	
	@GetMapping("/listagem")
	public String listagem() {
		return "times/listagem";
	}
	
	@PostMapping("/salvar")
	public String salvar(@RequestParam("escudo") MultipartFile file, @Valid Time time, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "times/cadastro";
		}
		timeService.salvarTime(time);
		attr.addFlashAttribute("sucesso", "ok");
		return "redirect:/times/novoTime";	
	}
	
	@GetMapping("/tabela/listagem")
	public ResponseEntity<?> findTimesCadastrados(HttpServletRequest req) {
		return ResponseEntity.ok(timeService.todos(req));
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirTime(@PathVariable("id") Long id,  RedirectAttributes attr) {
		timeService.remover(id);
		
		return "redirect:/times/listagem";
	}
	
	@GetMapping("/editar/{id}")
	public String preEditarTime(@PathVariable("id") Long id, ModelMap model, @AuthenticationPrincipal User user) {
		
		Time time = timeService.buscarTime(id);
		model.addAttribute("time", time);
		
		return "times/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(@RequestParam("escudo") MultipartFile file, @Valid Time time, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "times/cadastro";
		}
		timeService.editar(time);
		model.addAttribute("sucesso", "ok");
		
		return "times/cadastro";
	}
	
	@GetMapping("/buscarPeloPais")
	public ResponseEntity<?> procurarTimesDoPais(@RequestParam("pais") String pais) {
		List<Time> times = timeService.procurarTimesDoPais(pais);
		if (CollectionUtils.isNotEmpty(times)) {
			return ResponseEntity.ok(times);
		}	
		return ResponseEntity.notFound().build();
	}
	
	

}
