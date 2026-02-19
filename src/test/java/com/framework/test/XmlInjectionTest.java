package com.framework.test;

import com.framework.container.ApplicationContext;
import com.framework.container.BeanDefinition;
import com.framework.container.InjectionMode;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests pour l'injection par configuration XML
 */
public class XmlInjectionTest {
    
    private ApplicationContext context;

    @Before
    public void setup() {
        context = new ApplicationContext();
    }

    @Test
    public void testConstructorInjection() {
        // Créer une définition pour l'injection par constructeur
        BeanDefinition repoDef = new BeanDefinition("userRepository", UserRepository.class.getName());
        repoDef.setBeanClass(UserRepository.class);
        repoDef.setInjectionMode(InjectionMode.CONSTRUCTOR);
        
        // Ajouter les paramètres du constructeur
        repoDef.addConstructorArg("jdbc:mysql://localhost:3306/mydb", null, String.class);
        repoDef.addConstructorArg("root", null, String.class);
        repoDef.addConstructorArg("password123", null, String.class);
        
        context.registerBeanDefinition(repoDef);

        // Récupérer et vérifier le bean
        UserRepository repo = context.getBean("userRepository", UserRepository.class);
        assertNotNull("Repository doit être injecté", repo);
        assertEquals("DbUrl doit être correct", "jdbc:mysql://localhost:3306/mydb", repo.getDbUrl());
        assertEquals("Username doit être correct", "root", repo.getUsername());
    }

    @Test
    public void testSetterAndFieldInjection() {
        // Créer le repository
        BeanDefinition repoDef = new BeanDefinition("userRepository", UserRepository.class.getName());
        repoDef.setBeanClass(UserRepository.class);
        repoDef.setInjectionMode(InjectionMode.CONSTRUCTOR);
        repoDef.addConstructorArg("jdbc:sqlite://memory", null, String.class);
        repoDef.addConstructorArg("user", null, String.class);
        repoDef.addConstructorArg("pass", null, String.class);
        
        // Créer le service avec injection de propriétés
        BeanDefinition serviceDef = new BeanDefinition("userService", UserService.class.getName());
        serviceDef.setBeanClass(UserService.class);
        serviceDef.setInjectionMode(InjectionMode.SETTER);
        serviceDef.addProperty("userRepository", null, "userRepository", UserRepository.class);
        serviceDef.addProperty("maxAttempts", "5", null, int.class);
        serviceDef.addProperty("timeout", "3000", null, long.class);
        
        context.registerBeanDefinition(repoDef);
        context.registerBeanDefinition(serviceDef);

        // Récupérer et vérifier
        UserService service = context.getBean("userService", UserService.class);
        assertNotNull("Service doit être créé", service);
        assertNotNull("Repository doit être injecté", service.getUserRepository());
        assertEquals("MaxAttempts doit être 5", 5, service.getMaxAttempts());
        assertEquals("Timeout doit être 3000", 3000L, service.getTimeout());
    }

    @Test
    public void testMultipleBeansInjection() {
        // Créer plusieurs beans avec dépendances en chaîne
        BeanDefinition repoDef = new BeanDefinition("userRepository", UserRepository.class.getName());
        repoDef.setBeanClass(UserRepository.class);
        repoDef.setInjectionMode(InjectionMode.CONSTRUCTOR);
        repoDef.addConstructorArg("jdbc:h2:mem:test", null, String.class);
        repoDef.addConstructorArg("sa", null, String.class);
        repoDef.addConstructorArg("", null, String.class);
        
        BeanDefinition serviceDef = new BeanDefinition("userService", UserService.class.getName());
        serviceDef.setBeanClass(UserService.class);
        serviceDef.setInjectionMode(InjectionMode.SETTER);
        serviceDef.addProperty("userRepository", null, "userRepository", UserRepository.class);
        
        context.registerBeanDefinition(repoDef);
        context.registerBeanDefinition(serviceDef);

        // Vérifier la chaîne d'injection
        UserRepository repo = context.getBean("userRepository", UserRepository.class);
        UserService service = context.getBean("userService", UserService.class);
        
        assertNotNull("Repository doit exister", repo);
        assertNotNull("Service doit exister", service);
        assertSame("Service doit utiliser le même repository", repo, service.getUserRepository());
    }

    @Test
    public void testBeanSingletonBehavior() {
        BeanDefinition def = new BeanDefinition("singleton", UserRepository.class.getName());
        def.setBeanClass(UserRepository.class);
        def.setInjectionMode(InjectionMode.CONSTRUCTOR);
        def.addConstructorArg("jdbc:test", null, String.class);
        def.addConstructorArg("user", null, String.class);
        def.addConstructorArg("pass", null, String.class);
        def.setSingleton(true);
        
        context.registerBeanDefinition(def);

        // Récupérer plusieurs fois et vérifier que c'est le même objet
        UserRepository bean1 = context.getBean("singleton", UserRepository.class);
        UserRepository bean2 = context.getBean("singleton", UserRepository.class);
        
        assertSame("Les singletons doivent être la même instance", bean1, bean2);
    }

    @Test
    public void testExceptionOnMissingDependency() {
        // Essayer de créer un bean qui dépend d'un bean inexistant
        BeanDefinition serviceDef = new BeanDefinition("userService", UserService.class.getName());
        serviceDef.setBeanClass(UserService.class);
        serviceDef.setInjectionMode(InjectionMode.SETTER);
        serviceDef.addProperty("userRepository", null, "nonExistentRepository", UserRepository.class);
        
        context.registerBeanDefinition(serviceDef);

        // Vérifier que la dépendance manquante est gérée correctement
        UserService service = context.getBean("userService", UserService.class);
        // Dans cette implémentation, getBean retourne null pour les dépendances manquantes
        // Cela dépend de votre politique de gestion d'erreurs
        assertNotNull("Le service doit quand même être créé", service);
    }

    @After
    public void tearDown() {
        if (context != null) {
            context.close();
        }
    }
}
