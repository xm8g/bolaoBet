package com.bolao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mercado")
public class MercadosController {

	@GetMapping("/home")
	public String home() {
		return "bolao/odds";
	}
}
