package com.bolao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolao.entity.user.Participante;

public interface ParticipanteRepositoy extends JpaRepository<Participante, Long> {

	@Query("SELECT b.participantes FROM Bolao b WHERE b.id = :id")
	Optional<List<Participante>> findParticipanteByBolao(Long id);
	
}
