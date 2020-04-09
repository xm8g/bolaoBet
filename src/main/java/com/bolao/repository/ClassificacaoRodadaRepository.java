package com.bolao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolao.entity.ClassificacaoRodada;
import com.bolao.repository.projection.ClassificacaoGeralView;

@Repository
public interface ClassificacaoRodadaRepository extends JpaRepository<ClassificacaoRodada, Long> {

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClassificacaoRodada c WHERE c.bolao.id = :idBolao AND c.rodada = :rodada")
	boolean existsClassificacaoRodada(Long idBolao, Integer rodada);
	
	
	@Query("SELECT SUM(c.pontos) as somaPontuacaoGeral, "
			+ "SUM(c.pontuacaoPorCravadas) as somaCravadas, " 
			+ "SUM(c.pontuacaoPorPlacarDoVencedor) as somaGolsDoVencedor, "
			+ "SUM(c.pontuacaoPorAcertoDeSaldo) as somaSaldoDoVencedor, "
			+ "SUM(c.pontuacaoPorAcertoDeResultado) as somaResultados, "
			+ "SUM(c.pontuacaoPorEmpateGarantido) as somaEmpateGarantido, "
			+ "c.participante as participante, "
			+ "p.usuario as usuario "
			+ "FROM ClassificacaoRodada c JOIN c.participante p "
			+ "WHERE c.bolao.id = :idBolao "
			+ "GROUP BY c.participante")
	List<ClassificacaoGeralView> findClassificacaoByBolao(Long idBolao);
	
	@Query("SELECT SUM(c.pontos) as somaPontuacaoGeral, "
			+ "SUM(c.pontuacaoPorCravadas) as somaCravadas, " 
			+ "SUM(c.pontuacaoPorPlacarDoVencedor) as somaGolsDoVencedor, "
			+ "SUM(c.pontuacaoPorAcertoDeSaldo) as somaSaldoDoVencedor, "
			+ "SUM(c.pontuacaoPorAcertoDeResultado) as somaResultados, "
			+ "SUM(c.pontuacaoPorEmpateGarantido) as somaEmpateGarantido, "
			+ "c.participante as participante, "
			+ "p.usuario as usuario "
			+ "FROM ClassificacaoRodada c JOIN c.participante p "
			+ "WHERE c.bolao.id = :idBolao AND c.rodada = :rodada "
			+ "GROUP BY c.participante")
	List<ClassificacaoGeralView> findClassificacaoByRodada(Long idBolao, Integer rodada);
	
	
	@Query("SELECT DISTINCT c.rodada FROM ClassificacaoRodada c WHERE c.bolao.id = :idBolao")
	List<Integer> findRodadasClassifcadas(Long idBolao);
			
	
	@Modifying
	@Query("DELETE ClassificacaoRodada cr where cr.rodada = :rodada AND cr.bolao.id = :bolaoId")
	int deleteRodadaByBolao(Integer rodada, Long bolaoId);
}

