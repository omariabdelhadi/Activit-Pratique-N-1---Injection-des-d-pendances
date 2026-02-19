package com.framework.test;

import com.example.controller.UserController;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.framework.container.ApplicationContext;
import com.framework.container.BeanDefinition;
import com.framework.container.InjectionMode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests pour l'injection par annotations
 */
public class AnnotationInjectionTest {
    
    private ApplicationContext context;

    @Before
    public void setup() {
        context = new ApplicationContext();
    }

    @Test
    public void testComponentRegistration() {
        // Enregistrer les composants
        context.registerComponent(UserRepository.class);
        context.registerComponent(UserService.class);
        context.registerComponent(UserController.class);

        // Vérifier que les beans sont enregistrés
        assertTrue("Service doit être enregistré", context.containsBean("userService"));
        assertTrue("Controller doit être enregistré", context.containsBean("userController"));
    }

    @Test
    public void testFieldInjection() {
        // Configuration manuelle pour tester l'injection par field
        BeanDefinition repoDef = new BeanDefinition("userRepository", UserRepository.class.getName());
        repoDef.setBeanClass(UserRepository.class);
        repoDef.setInjectionMode(InjectionMode.CONSTRUCTOR);
        repoDef.addConstructorArg("jdbc:mysql://localhost/test", null, String.class);
        repoDef.addConstructorArg("root", null, String.class);
        repoDef.addConstructorArg("password", null, String.class);
        
        BeanDefinition serviceDef = new BeanDefinition("userService", UserService.class.getName());
        serviceDef.setBeanClass(UserService.class);
        serviceDef.setInjectionMode(InjectionMode.FIELD);
        serviceDef.addProperty("userRepository", null, "userRepository", UserRepository.class);
        serviceDef.addProperty("maxAttempts", "5", null, int.class);
        
        context.registerBeanDefinition(repoDef);
        context.registerBeanDefinition(serviceDef);

        // Récupérer et vérifier
        UserService service = context.getBean("userService", UserService.class);
        assertNotNull("UserService doit être créé", service);
        assertNotNull("UserRepository doit être injecté", service.getUserRepository());
        assertEquals("Max attempts doit être 5", 5, service.getMaxAttempts());
    }

    @Test
    public void testSetterInjection() {
        // Configuration pour l'injection par setter
        BeanDefinition repoDef = new BeanDefinition("userRepository", UserRepository.class.getName());
        repoDef.setBeanClass(UserRepository.class);
        repoDef.setInjectionMode(InjectionMode.CONSTRUCTOR);
        repoDef.addConstructorArg("jdbc:mysql://localhost/test", null, String.class);
        repoDef.addConstructorArg("admin", null, String.class);
        repoDef.addConstructorArg("admin123", null, String.class);
        
        BeanDefinition serviceDef = new BeanDefinition("userService", UserService.class.getName());
        serviceDef.setBeanClass(UserService.class);
        serviceDef.setInjectionMode(InjectionMode.SETTER);
        serviceDef.addProperty("userRepository", null, "userRepository", UserRepository.class);
        serviceDef.addProperty("timeout", "10000", null, long.class);
        
        context.registerBeanDefinition(repoDef);
        context.registerBeanDefinition(serviceDef);

        UserService service = context.getBean("userService", UserService.class);
        assertNotNull("UserService doit être créé", service);
        assertEquals("Timeout doit être 10000", 10000L, service.getTimeout());
    }

    @Test
    public void testAutowiredAnnotationType() {
        context.registerComponent(UserRepository.class);
        context.registerComponent(UserService.class);
        
        // Le repository est enregistré, le service peut le récupérer
        UserRepository repo = context.getBean("UserRepository", UserRepository.class);
        assertNotNull("Repository doit être disponible", repo);
    }

    @After
    public void tearDown() {
        if (context != null) {
            context.close();
        }
    }
}
