package com.bolao;

import org.directwebremoting.spring.DwrSpringServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@ImportResource(locations = "classpath:dwr-spring.xml")
@SpringBootApplication
public class BolaoBetApplication {

	public static void main(String[] args) {
		SpringApplication.run(BolaoBetApplication.class, args);
	}
	@Bean
	public LayoutDialect layoutDialect() {
	    return new LayoutDialect();
	}
	
	@Bean
	public ServletRegistrationBean<DwrSpringServlet> dwrSpringServlet() {
		DwrSpringServlet dwrServlet = new DwrSpringServlet();
		ServletRegistrationBean<DwrSpringServlet> registrationBean = 
				new ServletRegistrationBean<>(dwrServlet, "/dwr/*");
		
		registrationBean.addInitParameter("debug", "true");
		registrationBean.addInitParameter("activeReverseAjaxEnabled", "false");
		
		return registrationBean;
	}
}
