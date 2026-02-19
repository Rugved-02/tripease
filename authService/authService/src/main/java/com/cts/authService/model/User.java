package com.cts.authService.model;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name= "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String userId;
	
	private String name;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	private String mobile;
	
	private String password;
	
	@CreationTimestamp
    @Column(updatable = false, nullable = false)
	private java.time.Instant createdAt;
	
	

	
}


