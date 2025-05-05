package com.drloveapp.drloveapp_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drloveapp.drloveapp_backend.exception.ResourceNotFoundException;
import com.drloveapp.drloveapp_backend.model.User;
import com.drloveapp.drloveapp_backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;




    public Long getUserIdByEmail(String email) {
        User user = getUserByEmail(email);
        return user.getId();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + email));
    }
}