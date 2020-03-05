package com.bolao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bolao.entity.user.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("select u from Usuario u " 
			+ "where u.email like :email AND u.ativo = true")
	Optional<Usuario> findByEmailAndAtivo(String email);
	
	@Query("select u from Usuario u where u.email like :email")
	Usuario findByEmail(@Param("email") String email);
}
