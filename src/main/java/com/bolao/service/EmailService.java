package com.bolao.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private SpringTemplateEngine template;
	
	public void enviarEmailConfirmacaoCadastro(String destino, String codigo) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
		
		Context context = new Context();
		context.setVariable("titulo", "Bem Vindo ao BolãoBET");
		context.setVariable("texto", "Click no link abaixo para confirmar o cadastro.");
		context.setVariable("linkConfirmacao", "http://localhost:8080/usuario/confirmacao/cadastro?codigo=" + codigo);
		
		String html = template.process("email/confirmacao", context);
		helper.setTo(destino);
		helper.setText(html, true);
		helper.setSubject("Confirmação de Cadastro");
		helper.setFrom("alex.leleco@gmail.com");

		//Tem q ser a última linha
		helper.addInline("logo", new ClassPathResource("/static/image/goal.jpeg"));
		
		sender.send(message);
	}
	
	public void enviarPedidoRedefinicaoSenha(String destino, String verificador) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
		
		Context context = new Context();
		context.setVariable("titulo", "Redefinição de senha");
		context.setVariable("texto", "Para redefinir sua senha use o código de verificação quando exigido no formulário.");
		context.setVariable("verificador", verificador);
		
		String html = template.process("email/confirmacao", context);
		helper.setTo(destino);
		helper.setText(html, true);
		helper.setSubject("Redefinição de senha");
		helper.setFrom("alex.leleco@gmail.com");
		
		helper.addInline("logo", new ClassPathResource("/static/image/goal.jpeg"));
		
		sender.send(message);
	}
	
}
