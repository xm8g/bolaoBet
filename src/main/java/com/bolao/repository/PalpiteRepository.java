package com.bolao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolao.entity.bets.Palpite;
import com.bolao.repository.projection.PontuacaoDetalhada;

@Repository
public interface PalpiteRepository extends JpaRepository<Palpite, Long>{

	@Query("SELECT p FROM Palpite p WHERE p.partida.id = :idPartida AND p.participante.id = :idParticipante")
	Optional<Palpite> findByPartidaAndParticipante(Long idPartida, Long idParticipante);
	
	@Query("SELECT p FROM Palpite p WHERE p.partida.id = :idPartida AND p.valido = true AND p.processado = false")
	Optional<List<Palpite>> findPalpitesNaoProcessadosByPartida(Long idPartida);
	
	@Query("SELECT SUM(p.pontosGanhos.pontos) as pontuacaoGeral, "
			+ "SUM(CASE WHEN p.pontosGanhos.motivoPonto = 'EXATO' THEN p.pontosGanhos.pontos ELSE 0 END) as pontuacaoPorCravadas, "
			+ "SUM(CASE WHEN p.pontosGanhos.motivoPonto = 'GOLS_VENCEDOR' THEN p.pontosGanhos.pontos ELSE 0 END) as pontuacaoPorPlacarDoVencedor, "
			+ "SUM(CASE WHEN p.pontosGanhos.motivoPonto = 'SALDO_GOLS' THEN p.pontosGanhos.pontos ELSE 0 END) as pontuacaoPorAcertoDeSaldo, "
			+ "SUM(CASE WHEN p.pontosGanhos.motivoPonto = 'RESULTADO' THEN p.pontosGanhos.pontos ELSE 0 END) as pontuacaoPorAcertoDeResultado, "
			+ "SUM(CASE WHEN p.pontosGanhos.motivoPonto = 'EMPATE_GARANTIDO' THEN p.pontosGanhos.pontos ELSE 0 END) as pontuacaoPorEmpateGarantido"
			+ " FROM Palpite p WHERE p.participante.id = :idParticipante "
			+ "AND p.valido = true "
			+ "AND p.processado = false "
			+ "AND p.pontosGanhos.pontos > -1")
	PontuacaoDetalhada pontuacaoDetalhadaPorParticipante(Long idParticipante);
	
	@Modifying
	@Query("UPDATE Palpite p SET p.processado = true WHERE p.participante.id = :idParticipante " 
			+ "AND p.valido = true "
			+ "AND p.processado = false "
			+ "AND p.pontosGanhos.pontos > -1")
	int updatePalpitesNaoProcessadosByParticipante(Long idParticipante);
	
	@Modifying
	@Query("UPDATE Palpite p SET p.processado = false, p.pontosGanhos.pontos = -1, p.pontosGanhos.motivoPonto = NULL "
			+ "WHERE p.partida.id = :idPartida")
	int rollbackPalpitesPorPartida(Long idPartida);
	
}
