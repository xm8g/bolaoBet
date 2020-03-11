package com.bolao.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.bolao.entity.Bolao;
import com.bolao.entity.user.Usuario;
import com.bolao.service.BolaoService;
import com.bolao.service.CampeonatoService;
import com.bolao.service.UsuarioService;

@Controller
public class HomeController {

	@Autowired
	private CampeonatoService campeonatoService;
	
	@Autowired
	private BolaoService bolaoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping({ "/", "/home" })
	public String home(ModelMap model, @AuthenticationPrincipal User user) {
		
		model.addAttribute("campeonatos", campeonatoService.todos());
		if (user != null) {
			Usuario usuarioLogado = usuarioService.buscarPorEmail(user.getUsername());
			List<Bolao> boloesDoUsuario = bolaoService.boloesDoUsuario(usuarioLogado.getId());
			model.addAttribute("boloes", boloesDoUsuario);
		}
		
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
