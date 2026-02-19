package com.cts.authService.service;

import com.cts.authService.dto.UpdateUser;
import com.cts.authService.model.User;

public interface UserService {
	User saveUser(User user);
	boolean checkCredentials(String email,String password);
	User getUser(String userId);
	UpdateUser updateUser(String userId, UpdateUser dto);
}
