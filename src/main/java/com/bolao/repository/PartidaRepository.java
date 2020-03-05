package com.bolao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolao.entity.jogo.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

	@Query("select p from Partida p where p.rodada = :rodada")
	Optional<List<Partida>> findByRodada(int rodada);
}
