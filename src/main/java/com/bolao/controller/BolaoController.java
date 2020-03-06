package com.bolao.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolao.entity.Bolao;
import com.bolao.entity.jogo.Campeonato;
import com.bolao.entity.user.Usuario;
import com.bolao.service.BolaoService;
import com.bolao.service.CampeonatoService;
import com.bolao.service.UsuarioService;

@Controller
@RequestMapping("/bolao")
public class BolaoController {

	@Autowired
	private CampeonatoService campeonatoService;
	
	@Autowired
	private BolaoService bolaoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping({ "/criar/{id}" })
	public String criar(@PathVariable("id") Long id, Bolao bolao, ModelMap model) {
		
		Campeonato campeonato = campeonatoService.buscarCampeonato(id);
		bolao.setCampeonato(campeonato);
		bolao.setNomeCampeonato(campeonato.getNome());
		model.addAttribute("bolao", bolao);
		
		return "bolao/meu-bolao";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Bolao bolao, BindingResult result, @AuthenticationPrincipal User user, ModelMap model) {
		if (result.hasErrors()) {
			return "bolao/meu-bolao";
		}
		Usuario criadorDoBolao = usuarioService.buscarPorEmail(user.getUsername());
		bolao.setGestor(criadorDoBolao);
		bolaoService.salvarBolao(bolao);
		return "bolao/dashboard";	
	}
	
}
