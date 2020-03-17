package com.bolao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bolao.entity.Bolao;
import com.bolao.entity.user.Participante;
import com.bolao.entity.user.Usuario;
import com.bolao.service.UsuarioService;

@Controller
@RequestMapping("/apostas")
public class ApostasController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/home")
	public String home(Model model, @AuthenticationPrincipal User user, HttpServletRequest req) {
		
		Bolao bolao = (Bolao) req.getSession().getAttribute("bolao");
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		Participante p = bolao.getParticipantes().stream()
				.filter(participante -> usuario.getId().equals(participante.getUsuario().getId()))
				.findAny()
				.orElse(null);
		req.getSession().setAttribute("participante", p);
		model.addAttribute("participante", p);
		
		int rodadas = bolao.getCampeonato().getRodadas();
		model.addAttribute("rodadas", rodadas);
		
		return "bolao/apostas";
	}
}
