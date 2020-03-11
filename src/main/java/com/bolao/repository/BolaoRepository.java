package com.bolao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolao.entity.Bolao;

@Repository
public interface BolaoRepository extends JpaRepository<Bolao, Long> {

	@Query("select b from Bolao b where b.gestor.id = :id")
	Optional<List<Bolao>> findByGestorId(Long id);
	
	@Query("select b from Bolao b where :convidado MEMBER OF b.convidados")
	Optional<List<Bolao>> findBolaoByConvidado(String convidado);
	
	
}
