package com.bolao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolao.entity.Bolao;
import com.bolao.entity.jogo.Campeonato;
import com.bolao.entity.user.Participante;
import com.bolao.entity.user.Usuario;
import com.bolao.service.BolaoService;
import com.bolao.service.CampeonatoService;
import com.bolao.service.ParticipanteService;
import com.bolao.service.UsuarioService;

@Controller
@RequestMapping("/bolao")
public class BolaoController {

	@Autowired
	private CampeonatoService campeonatoService;
	
	@Autowired
	private BolaoService bolaoService;
	
	@Autowired
	private ParticipanteService participanteService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping({ "/criar/{id}" })
	public String criar(@PathVariable("id") Long id, Bolao bolao, ModelMap model) {
		
		Campeonato campeonato = campeonatoService.buscarCampeonato(id);
		bolao.setCampeonato(campeonato);
		bolao.setNomeCampeonato(campeonato.getNome());
		model.addAttribute("bolao", bolao);
		
		return "bolao/meu-bolao";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Bolao bolao, BindingResult result, @AuthenticationPrincipal User user, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "bolao/meu-bolao";
		}
		Usuario criadorDoBolao = usuarioService.buscarPorEmail(user.getUsername());
		bolao.setGestor(criadorDoBolao);
		bolaoService.salvarBolao(bolao);
		
		List<Bolao> boloesDoUsuario = bolaoService.boloesDoUsuario(criadorDoBolao.getId());
		attr.addFlashAttribute("boloesDoUsuario", boloesDoUsuario);
		
		return "redirect:/bolao/painel";	
	}
	
	@GetMapping("/painel")
	public String painelMeusBoloes(ModelMap model, @AuthenticationPrincipal User user) {
		
		Usuario criadorDoBolao = usuarioService.buscarPorEmail(user.getUsername());
		
		List<Bolao> boloesDoUsuario = bolaoService.boloesDoUsuario(criadorDoBolao.getId());
		model.addAttribute("boloesDoUsuario", boloesDoUsuario);
		
		return "bolao/meu-painel";
	}
	
	@GetMapping("/convites")
	public String painelMeusConvites(ModelMap model, @AuthenticationPrincipal User user) {
		
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		List<Bolao> boloesConvidados = bolaoService.boloesConvidados(usuario.getEmail(), usuario.getId());
		
		model.addAttribute("boloesConvidados", boloesConvidados);
		
		return "bolao/meus-convites";
	}
	
	@GetMapping("/listagem")
	public String listagem() {
		return "bolao/listagem";
	}
	
	@GetMapping("/tabela/listagem")
	public ResponseEntity<?> findBoloes(HttpServletRequest req) {
		return ResponseEntity.ok(bolaoService.todos(req));
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirTime(@PathVariable("id") Long id,  RedirectAttributes attr) {
		bolaoService.remover(id);
		
		return "redirect:/bolao/listagem";
	}
	
	@GetMapping("/join/{id}")
	public String entrarNoBolao(@PathVariable("id") Long id, @AuthenticationPrincipal User user, HttpServletRequest req) {
		
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		List<Participante> participantesDoBolao = participanteService.participantesDoBolao(id);
		Participante p;
		p = encontrarParticipantesDoBolao(participantesDoBolao, usuario.getId());
		Bolao bolao = bolaoService.buscarPorId(id);
		if (p == null) {
			p = new Participante();
			p.setUsuario(usuario);
			bolao.addParticipante(p);
			bolaoService.salvarBolao(bolao);
		}
		req.getSession().setAttribute("bolao", bolao);
		req.getSession().setAttribute("participante", p.getId());
		
		return "redirect:/classificacao/home";
	}

	private Participante encontrarParticipantesDoBolao(List<Participante> participantesDoBolao, Long userId) {
		for(Participante p: participantesDoBolao) {
			if (userId.equals(p.getUsuario().getId())) {
				return p;
			}
		}
		return null;
	}
	
}
