package com.spring.grocery.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.grocery.entity.Users;

public interface UserRepository extends JpaRepository<Users, String> {
	public Users findByUserName(String username);
	public Users findByEmail(String email);
}
