package com.bolao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolao.entity.bets.Palpite;

@Repository
public interface PalpiteRepository extends JpaRepository<Palpite, Long>{

	@Query("select p from Palpite p where p.partida.id = :idPartida AND p.participante.id = :idParticipante")
	Optional<Palpite> findByPartidaAndParticipante(Long idPartida, Long idParticipante);

}
