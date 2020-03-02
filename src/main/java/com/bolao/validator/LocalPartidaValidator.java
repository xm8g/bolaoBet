package com.bolao.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bolao.entity.Partida;

public class LocalPartidaValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Partida.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Partida partida = (Partida) target;
		
		String localPartida = partida.getLocal();
		
		if (StringUtils.isEmpty(localPartida) || localPartida.startsWith("<small>")) {
			errors.rejectValue("local", "NaoInformado.partida.local", "Não foi informado o local da partida.");
			return;
		}
		
	}

}
