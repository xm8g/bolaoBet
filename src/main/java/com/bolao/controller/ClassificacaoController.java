package com.bolao.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bolao.entity.Bolao;
import com.bolao.repository.projection.ClassificacaoLista;
import com.bolao.service.ClassificacaoService;

@Controller
@RequestMapping("/classificacao")
public class ClassificacaoController {

	@Autowired
	private ClassificacaoService classificacaoService;
	
	@GetMapping("/home")
	public String home(Model model, HttpServletRequest req, @AuthenticationPrincipal User user) {
		
		Bolao bolao = (Bolao) req.getSession().getAttribute("bolao");
		
		List<ClassificacaoLista> classificacao = classificacaoService.obterClassificacao(bolao, user);
		if (CollectionUtils.isEmpty(classificacao)) {
			model.addAttribute("classificacaoGeral", new ArrayList<ClassificacaoLista>());
		} else {
			Comparator<ClassificacaoLista> classificacaoPorPontosComparator = Comparator.comparing(ClassificacaoLista::getPontos);
		     
		    Collections.sort(classificacao, classificacaoPorPontosComparator.reversed());
			
			model.addAttribute("classificacaoGeral", classificacao);	
		}
		return "bolao/classificacao";
	}
	
	
}
