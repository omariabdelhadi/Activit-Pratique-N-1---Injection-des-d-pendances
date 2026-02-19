package com.example.repository;

import com.example.model.User;
import java.util.*;

/**
 * Repository pour les utilisateurs - Exemple pour l'injection par constructeur
 */
public class UserRepository {
    
    private String dbUrl;
    private String username;
    private String password;
    private Map<String, User> users = new HashMap<>();

    /**
     * Constructeur avec injection de dépendances
     */
    public UserRepository(String dbUrl, String username, String password) {
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
        System.out.println("UserRepository créé avec connexion: " + dbUrl);
    }

    public void save(User user) {
        users.put(user.getId(), user);
        System.out.println("Utilisateur sauvegardé: " + user);
    }

    public User findById(String id) {
        return users.get(id);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getUsername() {
        return username;
    }
}
