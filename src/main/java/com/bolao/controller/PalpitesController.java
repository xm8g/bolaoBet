package com.bolao.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bolao.entity.Bolao;
import com.bolao.entity.bets.Palpite;
import com.bolao.entity.user.Participante;
import com.bolao.entity.user.Usuario;
import com.bolao.service.PalpiteService;
import com.bolao.service.UsuarioService;

@Controller
@RequestMapping("/palpites")
public class PalpitesController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PalpiteService palpiteService;
	
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
		
		return "bolao/palpites";
	}
	
	@GetMapping("/partida")
	public ResponseEntity<Palpite> obterPalpiteDaPartidaByUser(@RequestParam("partidaId") Long idPartida, 
										@AuthenticationPrincipal User user, HttpServletRequest req) {

		Participante p = (Participante) req.getSession().getAttribute("participante");
		Palpite palpite = palpiteService.buscarPalpiteDoUsuarioDaPartida(idPartida, p.getId());
		
		if (palpite != null) {
			return ResponseEntity.ok(palpite);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<?> salvar(@Valid Palpite palpite, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			List<String> errors = new ArrayList<>();
			for(ObjectError error : result.getAllErrors()) {
				errors.add(error.getDefaultMessage());
			}
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		if (palpite.getId() != null) {
			palpiteService.atualizarPalpitePorId(palpite.getId(), palpite);
			return ResponseEntity.ok().build();
		}
		palpiteService.salvarPalpite(palpite);
		
		return ResponseEntity.ok().build();
	}
}
