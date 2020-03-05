package com.bolao.validator;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bolao.entity.jogo.Campeonato;

public class CampeonatoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Campeonato.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Campeonato campeonato = (Campeonato) target;
		
		if (campeonato != null && CollectionUtils.isEmpty(campeonato.getTimes())) {
			errors.rejectValue("times", "campeonato.times.qtde.vazia");
			return;
		}
		
		if (campeonato != null && CollectionUtils.isNotEmpty(campeonato.getTimes()) && campeonato.getTimes().size() > campeonato.getQtdeTimes()) {
			errors.rejectValue("times", "campeonato.times.qtde.superior");
		}

	}

}
