package com.bolao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bolao.entity.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

}
