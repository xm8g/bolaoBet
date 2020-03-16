package com.bolao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apostas")
public class ApostasController {

	@GetMapping("/home")
	public String home() {
		return "bolao/apostas";
	}
}
