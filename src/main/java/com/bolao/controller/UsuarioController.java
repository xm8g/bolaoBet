package com.bolao.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolao.entity.user.Avatar;
import com.bolao.entity.user.Usuario;
import com.bolao.service.AvatarService;
import com.bolao.service.UsuarioService;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AvatarService avatarService;

	@Autowired
	private ResourceLoader resourceLoader;

	@GetMapping("/cadastreSe")
	public String novoCadastro(Usuario usuario) {

		return "cadastrar-se";
	}

	@GetMapping("/editar/senha")
	public String abrirEditarSenha() {
		return "usuario/editar-senha";
	}

	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado() {

		return "fragments/cadastroSucesso";
	}

	@GetMapping("/cadastrar/avatar")
	public String cadastrarAvatar() {

		return "usuario/avatar";
	}

	@PostMapping("/avatar/salvar")
	public String salvar(@RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal User user,
			RedirectAttributes redirectAttributes) {
		Usuario u = usuarioService.buscarPorEmail(user.getUsername());
		if (!file.isEmpty()) {
			Avatar avatar = avatarService.storeFile(file);
			u.setAvatar(avatar);
			usuarioService.salvarAvatarDoUsuario(u);
			redirectAttributes.addFlashAttribute("sucesso", "Upload completado " + file.getOriginalFilename() + "!");
		} else {
			redirectAttributes.addFlashAttribute("falha", "Upload imcompleto.");
		}
		return "redirect:/usuario/cadastrar/avatar";
	}

	@PostMapping("/cadastro/salvar")
	public String salvar(Usuario usuario, BindingResult result) throws MessagingException {
		try {
			usuarioService.salvarCadastroUsuario(usuario);
		} catch (DataIntegrityViolationException e) {
			result.reject("email", "Ops...esse email já está cadastrado.");
			return "cadastrar-se";
		}
		return "redirect:/usuario/cadastro/realizado";
	}

	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2,
			@RequestParam("senha3") String s3, @AuthenticationPrincipal User user, RedirectAttributes attr) {
		if (!s1.equals(s2)) {
			attr.addFlashAttribute("falha", "Senhas não coincidem. Tente novamente.");
			return "redirect:/usuario/editar/senha";
		}
		Usuario u = usuarioService.buscarPorEmail(user.getUsername());
		if (!UsuarioService.isSenhaCorreta(s3, u.getSenha())) {
			attr.addFlashAttribute("falha", "Senha atual não confere. Tente novamente.");
			return "redirect:/usuario/editar/senha";
		}
		usuarioService.alterarSenha(u, s1);
		attr.addFlashAttribute("senhaAlterada", "ok");
		return "redirect:/";
	}

	@GetMapping(value = "/image")
	public @ResponseBody byte[] getImageUsuario(@AuthenticationPrincipal User user, Model model) throws IOException {
		Avatar avatar = avatarService.loadImageAvatar(user.getUsername());
		if (avatar != null) {
			model.addAttribute("temAvatar", true);
			return avatar.getData();
		} else {
			Resource resource = resourceLoader.getResource("classpath:/static/image/icons/iconfinder_user_1608727.png");
			InputStream in = resource.getInputStream();

			return IOUtils.toByteArray(in);
		}
	}
	
	@GetMapping("/confirmacao/cadastro")
	public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, RedirectAttributes attr) {
		usuarioService.ativarCadastroUsuario(codigo);
		
		attr.addFlashAttribute("alerta", "sucesso");
		attr.addFlashAttribute("titulo", "Cadastro Ativado!");
		attr.addFlashAttribute("texto", "Parabéns, seu cadastro está ativo.");
		attr.addFlashAttribute("subtexto", "Pode logar.");
		
		return "redirect:/login";
	}
	
	@GetMapping("/p/recuperar/senha")
	public String redefinirSenha(String email, ModelMap model) throws MessagingException {
		usuarioService.pedidoRedefinicaoSenha(email);
		model.addAttribute("sucesso", "Em instantes você receberá um email para redefinir sua senha");
		model.addAttribute("usuario",  new Usuario(email));
		
		return "usuario/recuperar-senha";
	}
	
	@GetMapping("/p/redefinir/senha")
	public String pedidoRedefinirSenha() {
		
		return "usuario/pedido-recuperar-senha";
	}
	
	@PostMapping("p/nova/senha")
	public String confirmacaoRedefinirSenha(Usuario usuario, ModelMap model) throws MessagingException {
		Usuario u = usuarioService.buscarPorEmail(usuario.getEmail());
		if (!u.getCodigoVerificador().equals(usuario.getCodigoVerificador())) {
			model.addAttribute("falha", "Código verificador não confere!");
			return "usuario/recuperar-senha";
		}
		u.setCodigoVerificador(null);
		usuarioService.alterarSenha(u, usuario.getSenha());
		model.addAttribute("alerta", "sucesso");
		model.addAttribute("titulo", "Senha redefinida!");
		model.addAttribute("texto", "Já pode logar no sistema.");
				
		return "login";
	}

}
