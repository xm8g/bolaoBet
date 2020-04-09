package com.bolao.repository.projection;

import com.bolao.entity.user.Participante;
import com.bolao.entity.user.Usuario;

public interface ClassificacaoGeralView {

	Integer getSomaPontuacaoGeral();
	
	Integer getSomaCravadas();
	
	Integer getSomaGolsDoVencedor();
	
	Integer getSomaSaldoDoVencedor();
	
	Integer getSomaResultados();
	
	Integer getSomaEmpateGarantido();
	
	Participante getParticipante();
	
	Usuario getUsuario();
}
