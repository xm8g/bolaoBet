package com.bolao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolao.business.AnalisadorPalpite;
import com.bolao.business.AnalisadorPalpiteCravada;
import com.bolao.business.AnalisadorPalpiteEmpateGarantido;
import com.bolao.business.AnalisadorPalpiteGolsVencedor;
import com.bolao.business.AnalisadorPalpiteResultadoSimples;
import com.bolao.business.AnalisadorPalpiteSaldoGols;
import com.bolao.business.ResultadoAnalisePalpite;
import com.bolao.entity.bets.Palpite;
import com.bolao.entity.jogo.Partida;
import com.bolao.entity.jogo.ResultadoPartida;

/**
 * 1- Selecionar o campeonato e a rodada
 * 2- Buscar pelas partidas encerradas.
 * 3- Buscar todos os palpites não processados e validos relacionados com estas partidas.
 * 4- Comparar os resultados do palpite de cada particpante
 * 5- Processar os pontos do palpite de cada participante e gravar na classe Palpite, bem como mudar o status do Palpite para 'processado'
 * 6- Somar/atualizar os pontos do participante (geral e categorias)
 * 7- Atualizar a linha de classificação da rodada do participante com seus pontos, se não achar a rodada, criar uma nova tupla
 */

@Service
public class ProcessadorRodada {

	@Autowired
	private PalpiteService palpiteService;
	
	
	public void processarPalpitesPorPartida(List<Partida> partidasEncerradasDaRodada) {
		for (Partida partida : partidasEncerradasDaRodada) {
			List<Palpite> palpitesDaPartidaParaSeremProcessados = palpiteService.palpitesDaPartidaParaSeremProcessados(partida.getId());
			for (Palpite palpite : palpitesDaPartidaParaSeremProcessados) {
				//de posse do palpite do particpante
				//passa-la para um processador de pontos
				//ao obter a pontuacao gravar na classe palpite a pontuação, o motivo
			}
		}
	}
	
	private void processaPalpite(Palpite palpite, Partida partida) {
		final AnalisadorPalpite analisaSeCravou = new AnalisadorPalpiteCravada();
		final AnalisadorPalpite analisaSeAcertouGolsDoVencedor = new AnalisadorPalpiteGolsVencedor();
		final AnalisadorPalpite analisaSeAcertouSaldo = new AnalisadorPalpiteSaldoGols();
		final AnalisadorPalpite analisaResultadoSimples = new AnalisadorPalpiteResultadoSimples();
		final AnalisadorPalpite analisaEmpateGarantido = new AnalisadorPalpiteEmpateGarantido();

		analisaSeCravou.proximaAnalise(analisaSeAcertouGolsDoVencedor);
		analisaSeAcertouGolsDoVencedor.proximaAnalise(analisaSeAcertouSaldo);
		analisaSeAcertouSaldo.proximaAnalise(analisaResultadoSimples);
		analisaResultadoSimples.proximaAnalise(analisaEmpateGarantido);
		
		ResultadoAnalisePalpite resultadoAnalisePalpite = analisaSeCravou.acertouNestaCategoria(palpite, partida.getResultado());
	}
}
