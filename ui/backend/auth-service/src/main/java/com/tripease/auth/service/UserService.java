package com.tripease.auth.service;

import com.tripease.auth.dto.UpdateUserDTO;
import com.tripease.auth.model.User;

public interface UserService {
    public User saveUserFromEncoded(String encodedData);
	User saveUser(User user);
	boolean checkCredentials(String email,String password);
	User getUser(String userId);
    User getUserProfile(String userId);
	User updateUser(String userId, UpdateUserDTO dto);
    String getUserIdByEmail(String email);
}
