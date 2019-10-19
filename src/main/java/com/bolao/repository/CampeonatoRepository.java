package com.bolao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bolao.entity.Campeonato;

@Repository
public interface CampeonatoRepository extends JpaRepository<Campeonato, Long> {

}
