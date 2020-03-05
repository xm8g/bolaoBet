package com.bolao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bolao.entity.jogo.ResultadoPartida;
import com.bolao.service.PartidaService;

@Controller
@RequestMapping("/resultados")
public class ResultadoPartidaController {

	@Autowired
	private PartidaService partidaService;	
	
	@PostMapping("/resultado/{id}")
	public ResponseEntity<?> setResultado(@PathVariable("id") Long id, ResultadoPartida resultado) {
		partidaService.editarPlacarFinal(id, resultado);
		
		return ResponseEntity.ok().build();	
	}
}
