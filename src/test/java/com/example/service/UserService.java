package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.framework.annotations.Autowired;
import com.framework.annotations.Component;

/**
 * Service pour les utilisateurs - Exemple pour l'injection par setter et field
 */
@Component("userService")
public class UserService {
    
    // Injection par field
    @Autowired
    private UserRepository userRepository;
    
    private int maxAttempts = 3;
    private long timeout = 5000;

    public UserService() {
        System.out.println("UserService créé");
    }

    // Injection par setter
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println("UserRepository injecté dans UserService");
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void createUser(String id, String name, String email) {
        User user = new User(id, name, email);
        userRepository.save(user);
        System.out.println("Utilisateur créé: " + user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id);
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public long getTimeout() {
        return timeout;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
