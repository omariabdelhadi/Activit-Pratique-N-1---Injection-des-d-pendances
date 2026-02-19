package com.example.controller;

import com.example.service.UserService;
import com.framework.annotations.Autowired;
import com.framework.annotations.Component;

/**
 * Controller pour les utilisateurs - Exemple pour l'injection par field
 */
@Component("userController")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    private String logLevel = "INFO";

    public UserController() {
        System.out.println("UserController créé");
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public void handleCreateUser(String id, String name, String email) {
        System.out.println("[" + logLevel + "] Création d'un nouvel utilisateur");
        if (userService != null) {
            userService.createUser(id, name, email);
        }
    }

    public void handleGetUser(String id) {
        System.out.println("[" + logLevel + "] Récupération de l'utilisateur: " + id);
        if (userService != null) {
            var user = userService.getUserById(id);
            System.out.println("Résultat: " + user);
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public String getLogLevel() {
        return logLevel;
    }
}
