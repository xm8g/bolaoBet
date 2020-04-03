package com.bolao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolao.entity.ClassificacaoRodada;
import com.bolao.repository.projection.ClassificacaoGeralView;

@Repository
public interface ClassificacaoRodadaRepository extends JpaRepository<ClassificacaoRodada, Long> {

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClassificacaoRodada c WHERE c.bolao.id = :idBolao AND c.rodada = :rodada")
	boolean existsClassificacaoRodada(Long idBolao, Integer rodada);
	
	
	@Query("SELECT SUM(c.pontos) as somaPontuacaoGeral, "
			+ "c.participante as participante, "
			+ "p.usuario as usuario "
			+ "FROM ClassificacaoRodada c JOIN c.participante p "
			+ "WHERE c.bolao.id = :idBolao "
			+ "GROUP BY c.participante")
	List<ClassificacaoGeralView> findClassificacaoByBolao(Long idBolao);
	
}
