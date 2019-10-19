package com.bolao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolao.entity.Perfil;
import com.bolao.entity.PerfilTipo;
import com.bolao.entity.Usuario;
import com.bolao.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional(readOnly = false)
	public void salvarCadastroUsuario(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuario.setAtivo(true);
		usuario.addPerfil(PerfilTipo.USUARIO);
		
		usuarioRepository.save(usuario);
		
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
	
}
