package com.example;

import com.example.controller.UserController;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.framework.container.ApplicationContext;
import com.framework.container.BeanDefinition;
import com.framework.container.InjectionMode;

/**
 * Classe de démonstration du framework d'injection de dépendances
 */
public class FrameworkDemo {

    public static void main(String[] args) {
        System.out.println("=== Mini Framework - Injection des Dépendances ===\n");

        // Démonstration 1 : Injection par Constructeur
        System.out.println("--- Démonstration 1 : Injection par Constructeur ---");
        demoConstructorInjection();

        System.out.println("\n--- Démonstration 2 : Injection par Setter ---");
        demoSetterInjection();

        System.out.println("\n--- Démonstration 3 : Injection par Field ---");
        demoFieldInjection();

        System.out.println("\n--- Démonstration 4 : Injection par Annotations ---");
        demoAnnotationInjection();

        System.out.println("\n=== Fin des démonstrations ===");
    }

    /**
     * Démontre l'injection par constructeur
     */
    private static void demoConstructorInjection() {
        ApplicationContext context = new ApplicationContext();

        // Créer une définition avec injection par constructeur
        BeanDefinition repoDef = new BeanDefinition("userRepository", UserRepository.class.getName());
        repoDef.setBeanClass(UserRepository.class);
        repoDef.setInjectionMode(InjectionMode.CONSTRUCTOR);
        repoDef.addConstructorArg("jdbc:mysql://localhost:3306/mydb", null, String.class);
        repoDef.addConstructorArg("root", null, String.class);
        repoDef.addConstructorArg("password", null, String.class);

        context.registerBeanDefinition(repoDef);

        UserRepository repository = context.getBean("userRepository", UserRepository.class);
        System.out.println("✓ UserRepository créé avec dépendances injectées par constructeur");
        System.out.println("  DB URL: " + repository.getDbUrl());
        System.out.println("  Username: " + repository.getUsername());

        context.close();
    }

    /**
     * Démontre l'injection par setter
     */
    private static void demoSetterInjection() {
        ApplicationContext context = new ApplicationContext();

        // Créer le repository
        BeanDefinition repoDef = new BeanDefinition("userRepository", UserRepository.class.getName());
        repoDef.setBeanClass(UserRepository.class);
        repoDef.setInjectionMode(InjectionMode.CONSTRUCTOR);
        repoDef.addConstructorArg("jdbc:sqlite://memory", null, String.class);
        repoDef.addConstructorArg("user", null, String.class);
        repoDef.addConstructorArg("pass", null, String.class);

        // Créer le service avec injection par setter
        BeanDefinition serviceDef = new BeanDefinition("userService", UserService.class.getName());
        serviceDef.setBeanClass(UserService.class);
        serviceDef.setInjectionMode(InjectionMode.SETTER);
        serviceDef.addProperty("userRepository", null, "userRepository", UserRepository.class);
        serviceDef.addProperty("maxAttempts", "5", null, int.class);
        serviceDef.addProperty("timeout", "3000", null, long.class);

        context.registerBeanDefinition(repoDef);
        context.registerBeanDefinition(serviceDef);

        UserService service = context.getBean("userService", UserService.class);
        System.out.println("✓ UserService créé avec dépendances injectées par setter");
        System.out.println("  Max Attempts: " + service.getMaxAttempts());
        System.out.println("  Timeout: " + service.getTimeout());
        System.out.println("  Repository injecté: " + (service.getUserRepository() != null ? "Oui" : "Non"));

        context.close();
    }

    /**
     * Démontre l'injection par field
     */
    private static void demoFieldInjection() {
        ApplicationContext context = new ApplicationContext();

        // Créer le repository
        BeanDefinition repoDef = new BeanDefinition("userRepository", UserRepository.class.getName());
        repoDef.setBeanClass(UserRepository.class);
        repoDef.setInjectionMode(InjectionMode.CONSTRUCTOR);
        repoDef.addConstructorArg("jdbc:h2:mem:test", null, String.class);
        repoDef.addConstructorArg("sa", null, String.class);
        repoDef.addConstructorArg("", null, String.class);

        // Créer le service pour l'injection field
        BeanDefinition serviceDef = new BeanDefinition("userService", UserService.class.getName());
        serviceDef.setBeanClass(UserService.class);
        serviceDef.setInjectionMode(InjectionMode.FIELD);
        serviceDef.addProperty("userRepository", null, "userRepository", UserRepository.class);

        // Créer le controller
        BeanDefinition controllerDef = new BeanDefinition("userController", UserController.class.getName());
        controllerDef.setBeanClass(UserController.class);
        controllerDef.setInjectionMode(InjectionMode.FIELD);
        controllerDef.addProperty("userService", null, "userService", UserService.class);
        controllerDef.addProperty("logLevel", "DEBUG", null, String.class);

        context.registerBeanDefinition(repoDef);
        context.registerBeanDefinition(serviceDef);
        context.registerBeanDefinition(controllerDef);

        UserController controller = context.getBean("userController", UserController.class);
        System.out.println("✓ UserController créé avec dépendances injectées par field");
        System.out.println("  Service injecté: " + (controller.getUserService() != null ? "Oui" : "Non"));
        System.out.println("  Log Level: " + controller.getLogLevel());

        // Tester la chaîne d'injection
        System.out.println("\n  Chaîne d'injection:");
        System.out.println("  Controller -> Service -> Repository");
        controller.handleCreateUser("1", "Jean Dupont", "jean@example.com");

        context.close();
    }

    /**
     * Démontre l'injection par annotations
     */
    private static void demoAnnotationInjection() {
        ApplicationContext context = new ApplicationContext();

        // Enregistrer les composants annotés
        context.registerComponent(UserRepository.class);
        context.registerComponent(UserService.class);
        context.registerComponent(UserController.class);

        System.out.println("✓ Composants enregistrés via annotations @Component");

        // Récupérer un composant
        UserService service = context.getBean("userService", UserService.class);
        if (service != null) {
            System.out.println("✓ UserService récupéré du contexte");

            // Créer un utilisateur
            service.createUser("2", "Marie Martin", "marie@example.com");
            System.out.println("✓ Utilisateur créé via le service injecté");

            // Récupérer l'utilisateur
            var user = service.getUserById("2");
            if (user != null) {
                System.out.println("✓ Utilisateur récupéré: " + user);
            }
        }

        context.close();
    }
}
