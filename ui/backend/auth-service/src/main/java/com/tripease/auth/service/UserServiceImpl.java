package com.tripease.auth.service;

import com.tripease.auth.dto.UserRegistrationRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tripease.auth.dto.UpdateUserDTO;
import com.tripease.auth.exception.UserAlreadyExistsException;
import com.tripease.auth.exception.UserNotFoundException;
import com.tripease.auth.model.User;
import com.tripease.auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
	
	private final UserRepository repo;
	private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public User saveUserFromEncoded(String encodedData) {
        try {
            // 1. Decode Base64 to JSON bytes
            byte[] decodedBytes = Base64.getDecoder().decode(encodedData);

            // 2. Map JSON to your DTO
            UserRegistrationRequestDTO dto = objectMapper.readValue(decodedBytes, UserRegistrationRequestDTO.class);

            // 3. Map DTO to Entity
            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setMobile(dto.getMobile());
            user.setPassword(dto.getPassword()); // Raw password from Base64

            // 4. Pass the entity to your existing saveUser method
            return this.saveUser(user);

        } catch (IllegalArgumentException e) {
            log.error("Invalid Base64 string received", e);
            throw new RuntimeException("Data corruption during transmission", e);
        }
    }

    // Your existing method remains exactly as it was
    @Override
    @Transactional
    public User saveUser(User user) {
        log.info("Attempting to register new user with email: {}", user.getEmail());

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists!");
        }

        // BCrypt hashing
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repo.save(user);
    }
	
	
	
	

	@Override
	public boolean checkCredentials(String encodedEmail, String encodedPassword) {

//        byte[] emailBytes = Base64.getDecoder().decode(encodedEmail);
//        String decodedEmail = new String(emailBytes, StandardCharsets.UTF_8);
        String decodedEmail = encodedEmail;
//
        // 2. Decode the Password
//        byte[] passBytes = Base64.getDecoder().decode(encodedPassword);
//        String decodedPassword = new String(passBytes, StandardCharsets.UTF_8);
        String decodedPassword = encodedPassword;

        log.info("Authenticating user: {}",decodedEmail );
		User user = repo.findByEmail(decodedEmail)
		        .orElseThrow(() -> {
		        	log.warn("Login failed: User not found with email {}", decodedEmail);
		        	return new UserNotFoundException("No account found with email: " + decodedEmail);
		        });

		if (!passwordEncoder.matches(decodedPassword, user.getPassword())) {
			log.warn("Login failed: Incorrect password for user {}", decodedEmail);
	        throw new RuntimeException("Invalid email or password");
	    }
		log.info("User {} successfully authenticated", decodedEmail);
        return true; // User found
    }

	
	
	
	
	@Override
	public User getUser(String email) {
		// TODO Auto-generated method stub
		log.debug("Fetching profile details for user: {}", email);
		
		return repo.findByEmail(email)
		        .orElseThrow(() ->{ 
		        	log.error("Profile fetch failed: User {} not found", email);
		        	return new UserNotFoundException("User not found with ID: " + email);
		        	});
	}

    @Override
    public String getUserIdByEmail(String email) {
        return repo.findByEmail(email)
                .map(User::getUserId)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public User getUserProfile(String userId) {
        // TODO Auto-generated method stub
        log.debug("Fetching profile details for user: {}", userId);

        return repo.findByUserId(userId)
                .orElseThrow(() ->{
                    log.error("Profile fetch failed: User {} not found", userId);
                    return new UserNotFoundException("User not found with ID: " + userId);
                });
    }
	
	
	
	
	
	@Override
	public User updateUser(String email, UpdateUserDTO dto) {
		log.info("Initiating profile update for user: {}", email);
		
	    User user = repo.findByEmail(email)
	    		.orElseThrow(() ->{
	    			log.error("Update failed: User {} not found", email);
	    			return new UserNotFoundException("User not found with ID: " + email);
	    			});

	    // 2. Map DTO -> Entity
	    user.setName(dto.getName());
	    user.setMobile(dto.getMobile());
	    
	    
	    // 4. Save the Entity
	    
	    log.info("Profile successfully updated for user: {}", email);
	    // 5. Return the DTO (The state of the updated data)
	    return repo.save(user);
	}
	
	
	

}
