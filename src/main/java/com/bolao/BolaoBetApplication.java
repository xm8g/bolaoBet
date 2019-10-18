package com.bolao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication
public class BolaoBetApplication {

	public static void main(String[] args) {
		SpringApplication.run(BolaoBetApplication.class, args);
	}
	@Bean
	public LayoutDialect layoutDialect() {
	    return new LayoutDialect();
	}
}
