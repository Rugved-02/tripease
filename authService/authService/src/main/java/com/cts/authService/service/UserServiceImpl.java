package com.cts.authService.service;

import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.authService.dto.UpdateUser;
import com.cts.authService.exception.UserAlreadyExistsException;
import com.cts.authService.exception.UserNotFoundException;
import com.cts.authService.model.User;
import com.cts.authService.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
	
	UserRepository repo;
	PasswordEncoder passwordEncoder;
	
	
	
	
	@Override
	@Transactional
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		System.out.println("UserServiceImpl::saveUser()");
		
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
		    throw new RuntimeException("Password cannot be empty");
		}
		
		if (repo.findByEmail(user.getEmail()).isPresent()) {
	        throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists!");
	    }
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
	    
		return repo.save(user);
	}
	
	
	
	

	@Override
	public boolean checkCredentials(String email, String password) {
       
		User user = repo.findByEmail(email)
		        .orElseThrow(() -> new UserNotFoundException("No account found with email: " + email));

		if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new RuntimeException("Invalid email or password");
	    }
        
        return true; // User found
    }

	
	
	
	
	@Override
	public User getUser(String email) {
		// TODO Auto-generated method stub
		System.out.println("UserServiceImpl::getUser()");
		
		return repo.findByEmail(email)
		        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + email));
	}
	
	
	
	
	
	@Override
	public UpdateUser updateUser(String email, UpdateUser dto) {
	
	    User user = repo.findByEmail(email)
	    		.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + email));

	    // 2. Map DTO -> Entity
	    user.setName(dto.getName());
	    user.setMobile(dto.getMobile());
	    
	    // 3. Handle Password (Security Check)
	    // If the DTO contains a new password, hash it before saving
	    if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
	        user.setPassword(passwordEncoder.encode(dto.getPassword()));
	    }
	    
	    // 4. Save the Entity
	    repo.save(user);
	    // 5. Return the DTO (The state of the updated data)
	    return dto; 
	}
	
	
	

}
