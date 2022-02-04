package com.bolao.repository.projection;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ClassificacaoChart {

	String nome;
	List<Integer> historicoClassificacoes;
	
	public void addRanking(Integer posicao) {
		if (historicoClassificacoes == null) {
			historicoClassificacoes = new ArrayList<>();
		}
		historicoClassificacoes.add(posicao);
	}
}
