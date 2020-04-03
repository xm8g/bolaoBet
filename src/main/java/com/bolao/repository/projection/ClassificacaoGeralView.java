package com.bolao.repository.projection;

import com.bolao.entity.user.Participante;
import com.bolao.entity.user.Usuario;

public interface ClassificacaoGeralView {

	Integer getSomaPontuacaoGeral();
	
	Participante getParticipante();
	
	Usuario getUsuario();
}
