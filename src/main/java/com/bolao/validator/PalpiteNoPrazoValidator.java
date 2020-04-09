package com.bolao.validator;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.bolao.entity.bets.Palpite;

public class PalpiteNoPrazoValidator implements ConstraintValidator<PalpiteNoPrazo, Palpite> {

	@Override
	public boolean isValid(Palpite palpite, ConstraintValidatorContext context) {

		if (palpite != null && !palpite.getPartida().isEncerrada() && LocalDateTime.now().isAfter(palpite.getPartida().getData())) {
			return false;
		}
		return true;
	}
}
