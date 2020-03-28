package com.bolao.service;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.bolao.entity.user.Perfil;
import com.bolao.entity.user.PerfilTipo;
import com.bolao.entity.user.Usuario;
import com.bolao.exception.AcessoNegadoException;
import com.bolao.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Transactional(readOnly = false)
	public void salvarCadastroUsuario(Usuario usuario) throws MessagingException {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuario.setAtivo(false);
		usuario.addPerfil(PerfilTipo.USUARIO);
		
		usuarioRepository.save(usuario);
		
		emailDeConfirmacaoDeCadastro(usuario.getEmail());
	}
	
	@Transactional(readOnly = false)
	public void salvarAvatarDoUsuario(Usuario usuario) {
		
		usuarioRepository.save(usuario);
		
	}
	
	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Override @Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmailEAtivo(username).orElseThrow(() -> new UsernameNotFoundException("Usuário " + username + " não está ativo ou não foi encontrado."));
		
		return new User(
				usuario.getEmail(), 
				usuario.getSenha(), 
				AuthorityUtils.createAuthorityList(getAuthorities(usuario.getPerfis())));
	}
	
	public static void main(String... args) {
		String crypt = new BCryptPasswordEncoder().encode("123456");
//		//boolean matches = new BCryptPasswordEncoder().matches("123456", "$2a$10$SRg7H75roSECL5..z6R23.J9/ThCyJBS4VPNrYgLzgGj0vkRxF/tC");
		System.out.println(crypt);
	}

	private String[] getAuthorities(List<Perfil> perfis) {
		return perfis.stream().map(p -> p.getDesc()).toArray(String[]::new);
	}
	
	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmailEAtivo(String email) {
		return usuarioRepository.findByEmailAndAtivo(email);
	}

	public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
	}
	
	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		usuarioRepository.save(usuario);
	}
	
	public void emailDeConfirmacaoDeCadastro(String email) throws MessagingException {
		String codigo = Base64Utils.encodeToString(email.getBytes());
		emailService.enviarEmailConfirmacaoCadastro(email, codigo);
	}
	
	@Transactional(readOnly = false)
	public void ativarCadastroUsuario(String codigo) {
		String email = new String(Base64Utils.decodeFromString(codigo));
		Usuario u = buscarPorEmail(email);
		if (u.hasNotId()) {
			throw new AcessoNegadoException("Não foi possível ativar seu cadastro. Usuario sem ID.");
		}
		u.setAtivo(true);
	}

	@Transactional(readOnly = false)
	public void pedidoRedefinicaoSenha(String email) throws MessagingException {
		Usuario u = buscarPorEmailEAtivo(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
		
		String verificador = RandomStringUtils.randomAlphanumeric(6);
		u.setCodigoVerificador(verificador);
		
		emailService.enviarPedidoRedefinicaoSenha(email, verificador);
	}
}
