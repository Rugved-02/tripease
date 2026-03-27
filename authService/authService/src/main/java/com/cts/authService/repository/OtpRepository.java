package com.cts.authService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.cts.authService.model.Otp;

import jakarta.transaction.Transactional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
	Optional<Otp> findByEmail(String email);

    @Modifying
    @Transactional
    void deleteByEmail(String email);
}
