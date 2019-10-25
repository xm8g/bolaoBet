package com.bolao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bolao.entity.Campeonato;
import com.bolao.entity.Partida;
import com.bolao.service.CampeonatoService;
import com.bolao.service.PartidaService;

@Controller
@RequestMapping("/partidas")
public class PartidaController {

	@Autowired
	private PartidaService partidaService;
	
	@Autowired
	private CampeonatoService campeonatoService;
	
	@GetMapping("/nova")
	public String novaPartida(Partida partida, Model model) {
		return "partidas/cadastro";
	}
	
	@GetMapping("/listagem")
	public String listagem() {
		return "partidas/listagem";
	}
	
	@GetMapping("/resultados")
	public String resultados() {
		return "partidas/resultados";
	}
	
	@ModelAttribute("campeonatos")
	public List<Campeonato> listaCampeonatos() {
		
		return campeonatoService.todos();
	}
}
