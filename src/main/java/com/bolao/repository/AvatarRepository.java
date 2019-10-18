package com.bolao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolao.entity.Avatar;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

}
