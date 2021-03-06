package com.bolao.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolao.entity.user.PerfilTipo;
import com.bolao.service.UsuarioService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String USUARIO = PerfilTipo.USUARIO.getDesc();

	@Autowired
	private UsuarioService usuarioService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// acessos públicos liberados
				.antMatchers("/webjars/**", "/css/**", "/js/**", "/image/**").permitAll().antMatchers("/", "/home")
				.permitAll()
				.antMatchers("/usuario/cadastreSe", 
							"/usuario/editar/senha", 
							"/usuario/cadastro/salvar",
							"/usuario/cadastro/realizado", 
							"/usuario/cadastrar/avatar", 
							"/usuario/image",
							"/usuario/avatar/salvar",
							"/usuario/confirmacao/cadastro",
							"/usuario/p/**",
							"/usuario/confirmar/senha").permitAll()
				// acessos privados ADMIN
				.antMatchers("/times/**", 
							"/campeonatos/**", "/processador/**", "/resultados/**").hasAuthority(ADMIN) 
				.antMatchers("/partidas/nova", 
							"/partidas/resultados", 
							"/partidas/salvar", 
							"/partidas/delete/*").hasAuthority(ADMIN)
				.antMatchers("/bolao/tabela/listagem", 
							"bolao/excluir/**").hasAuthority(ADMIN)
				// acessos privados USUARIO
				.antMatchers("/bolao/criar", 
							"/bolao/salvar", 
							"/bolao/painel", 
							"/bolao/dashboard", 
							"/bolao/convites",
							"/classificacao/**",
							"/palpites/**")
				.hasAnyAuthority(ADMIN, USUARIO)
				.antMatchers("/partidas/tabela/listagem/*").hasAnyAuthority(ADMIN, USUARIO)
				// MEDICO)
				// .antMatchers("/medicos/**").hasAuthority(MEDICO).antMatchers("/especialidades/titulo")
				// .hasAnyAuthority(MEDICO, ADMIN,
				// PACIENTE).antMatchers("/especialidades/datatables/server/medico/*")
				// .hasAnyAuthority(MEDICO, ADMIN)
				// acessos para especialidades médicas
				// .antMatchers("/especialidades/**").hasAuthority(ADMIN)
				// acessos para pacientes
				// .antMatchers("/pacientes/**").hasAuthority(PACIENTE)

				.anyRequest()
				.authenticated()
				.and()
				.formLogin().loginPage("/login").defaultSuccessUrl("/", true)
				.failureUrl("/login-error").permitAll()
				.and()
				.logout().logoutSuccessUrl("/")
				.and()
				.exceptionHandling().accessDeniedPage("/acesso-negado")
				.and()
				.rememberMe()
				.and()
				.csrf().disable();

	}

	/**
	 * Este método cuida da autenticação no formulário de login. Deve possuir um
	 * service que implementa a interface UserDetailsService
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
