package com.bolao.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.bolao.entity.jogo.Campeonato;
import com.bolao.service.CampeonatoService;

@Controller
public class HomeController {

	@Autowired
	private CampeonatoService campeonatoService;
	
	@GetMapping({ "/", "/home" })
	public String home(ModelMap model) {
		
		model.addAttribute("campeonatos", campeonatoService.todos());
		
		return "home";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/login-error") //o controle desta URI está no SecurityConfig
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas");
		model.addAttribute("texto", "Login ou senha incorretos. Tente novamente.");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados.");
		return "login"; //login.html
	}
	
	@GetMapping("/acesso-negado") //o controle desta URI está no SecurityConfig
	public String acessoNegado(ModelMap model, HttpServletResponse response) {
		model.addAttribute("status", response.getStatus());
		model.addAttribute("error", "Acesso Negado!");
		model.addAttribute("message", "Você não tem acesso a essa área.");
		return "error"; //error.html
	}
}
