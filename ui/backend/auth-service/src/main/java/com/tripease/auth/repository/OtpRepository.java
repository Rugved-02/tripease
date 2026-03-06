package com.tripease.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tripease.auth.model.Otp;

import jakarta.transaction.Transactional;
@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
	Optional<Otp> findByEmail(String email);

    @Modifying
    @Transactional
    void deleteByEmail(String email);
}
