package com.bolao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bolao.entity.Campeonato;
import com.bolao.entity.Partida;
import com.bolao.service.CampeonatoService;

@Controller
@RequestMapping("/campeonatos")
public class CampeonatoController {

	@Autowired
	private CampeonatoService campeonatoService;
	
	@GetMapping("/novo")
	public String novoCampeonato(Campeonato campeonatopartida, Model model) {
		return "campeonatos/cadastro";
	}
	
	@GetMapping("/listagem")
	public String listagem() {
		return "campeonatos/listagem";
	}
}
