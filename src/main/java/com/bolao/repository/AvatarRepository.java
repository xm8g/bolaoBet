package com.bolao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolao.entity.user.Avatar;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

}
