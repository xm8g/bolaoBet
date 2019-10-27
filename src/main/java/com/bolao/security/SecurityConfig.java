package com.bolao.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolao.entity.PerfilTipo;
import com.bolao.service.UsuarioService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String USUARIO = PerfilTipo.USUARIO.getDesc();
	private static final String GESTOR = PerfilTipo.GESTOR.getDesc();
	private static final String CONVIDADO = PerfilTipo.CONVIDADO.getDesc();
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			// acessos públicos liberados
			.antMatchers("/webjars/**", "/css/**", "/js/**", "/dwr/**", "/image/**").permitAll()
			.antMatchers("/", "/home").permitAll()
			.antMatchers("/usuario/cadastreSe",
						"/usuario/editar/senha", 
						"/usuario/cadastro/salvar", 
						"/usuario/cadastro/realizado",
						"/usuario/cadastrar/avatar",
						"/usuario/image",
						"/usuario/avatar/salvar",
						"/usuario/confirmar/senha").permitAll()
			.antMatchers("/times/**", "/campeonatos/**").hasAuthority(ADMIN)
			// acessos para medicos
			//.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(ADMIN, MEDICO)
			//.antMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(PACIENTE, MEDICO)
			//.antMatchers("/medicos/**").hasAuthority(MEDICO).antMatchers("/especialidades/titulo")
			//.hasAnyAuthority(MEDICO, ADMIN, PACIENTE).antMatchers("/especialidades/datatables/server/medico/*")
			//.hasAnyAuthority(MEDICO, ADMIN)
			// acessos para especialidades médicas
			//.antMatchers("/especialidades/**").hasAuthority(ADMIN)
			// acessos para pacientes
			//.antMatchers("/pacientes/**").hasAuthority(PACIENTE)

			.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login-error")
				.permitAll()
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
				.exceptionHandling()
				.accessDeniedPage("/acesso-negado")
			.and()
				.rememberMe();

	}
	
	/**
	 * Este método cuida da autenticação no formulário de login.
	 * Deve possuir um service que implementa a interface UserDetailsService 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
