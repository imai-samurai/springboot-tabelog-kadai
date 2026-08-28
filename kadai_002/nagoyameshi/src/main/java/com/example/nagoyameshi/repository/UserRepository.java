package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);

	Optional<User> findByStripeCustomerId(String stripeCustomerId);

	//admin用
	Page<User> findByEmailLike(String keyword, Pageable pageable);

	long countByRoleId(Integer roleId);

}
