package com.bolao.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolao.entity.Bolao;
import com.bolao.entity.ClassificacaoRodada;
import com.bolao.repository.ClassificacaoRodadaRepository;
import com.bolao.repository.projection.ClassificacaoGeralView;
import com.bolao.repository.projection.ClassificacaoLista;

@Service
public class ClassificacaoService {

	@Autowired
	private ClassificacaoRodadaRepository classificacaoRodadaRepository;
	
	@Transactional(readOnly = false)
	public void salvarClassificacaoDaRodada(ClassificacaoRodada classificacao) {
		classificacaoRodadaRepository.save(classificacao);
	}
	
	@Transactional(readOnly = false)
	public void desclassificarRodadaDoBolao(Integer rodada, Long bolaoId) {
		classificacaoRodadaRepository.deleteRodadaByBolao(rodada, bolaoId);
	}
	
	public boolean classificacaoJaFoiProcessada(Long idBolao, Integer rodada) {
		return classificacaoRodadaRepository.existsClassificacaoRodada(idBolao, rodada);
	}
	
	public List<Integer> buscaRodadasDoBolaoQueJaForamClassificadas(Long idBolao) {
		return classificacaoRodadaRepository.findRodadasClassifcadas(idBolao);
	}
	
	@Transactional(readOnly = true)
	public List<ClassificacaoLista> obterClassificacao(Bolao bolao, User user, Integer rodada) {
		List<ClassificacaoGeralView> viewBasica = rodada == 0 ? 
				classificacaoRodadaRepository.findClassificacaoByBolao(bolao.getId()) : 
				classificacaoRodadaRepository.findClassificacaoByRodada(bolao.getId(), rodada);
		List<ClassificacaoLista> classificacaoGeral = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(viewBasica)) {
			for (ClassificacaoGeralView view : viewBasica) {
				classificacaoGeral.add(classificaUmParticipanteNaTabela(view, user));
			}
		}
		return classificacaoGeral;
	}

	private ClassificacaoLista classificaUmParticipanteNaTabela(ClassificacaoGeralView view, User user) {
		ClassificacaoLista classificacaoLista = new ClassificacaoLista();
		classificacaoLista.setPontos(view.getSomaPontuacaoGeral());
		classificacaoLista.setAvatar(Base64.getEncoder().encodeToString(view.getUsuario().getAvatar().getData()));
		classificacaoLista.setNomeParticipante(view.getUsuario().getEmail());
		classificacaoLista.setPontuacaoPorCravadas(view.getSomaCravadas());
		classificacaoLista.setPontuacaoPorPlacarDoVencedor(view.getSomaGolsDoVencedor());
		classificacaoLista.setPontuacaoPorAcertoDeSaldo(view.getSomaSaldoDoVencedor());
		classificacaoLista.setPontuacaoPorAcertoDeResultado(view.getSomaResultados());
		classificacaoLista.setPontuacaoPorEmpateGarantido(view.getSomaEmpateGarantido());
		classificacaoLista.setCurrentUser(view.getParticipante().getUsuario().getEmail().equals(user.getUsername()));
		
		return classificacaoLista;
	}
}
