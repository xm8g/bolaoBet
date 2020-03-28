package com.bolao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolao.entity.bets.Palpite;

@Repository
public interface PalpiteRepository extends JpaRepository<Palpite, Long>{

	@Query("select p from Palpite p where p.partida.id = :idPartida AND p.participante.id = :idParticipante")
	Optional<Palpite> findByPartidaAndParticipante(Long idPartida, Long idParticipante);
	
	@Query("select p from Palpite p where p.partida.id = :idPartida AND p.valido = true AND p.processado = false")
	Optional<List<Palpite>> findPalpitesNaoProcessadosByPartida(Long idPartida);
	
}
