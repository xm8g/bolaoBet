package com.bolao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolao.entity.user.Participante;

public interface ParticipanteRepositoy extends JpaRepository<Participante, Long> {

	@Query("select b.participantes from Bolao b where b.id = :id")
	Optional<List<Participante>> findParticipanteByBolao(Long id);
	
}
