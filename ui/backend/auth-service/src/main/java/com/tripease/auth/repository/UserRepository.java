package com.tripease.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripease.auth.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);

    Optional<User> findByUserId(String userId);

	boolean existsByEmail(String email);

    long count();
}
