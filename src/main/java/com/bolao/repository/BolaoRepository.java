package com.bolao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bolao.entity.Bolao;

@Repository
public interface BolaoRepository extends JpaRepository<Bolao, Long> {

}
