package com.bolao.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bolao.entity.Time;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {

	Page<Time> findAll(Pageable pageable);
	
	@Query("select t from Time t where 	t.pais = :pais")
	List<Time> findByPais(@Param("pais") String pais);
}
