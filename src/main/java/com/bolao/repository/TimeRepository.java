package com.bolao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bolao.entity.Time;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {

	Page<Time> findAll(Pageable pageable);
}
