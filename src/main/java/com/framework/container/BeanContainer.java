package com.framework.container;

import com.framework.annotations.Component;
import com.framework.annotations.Autowired;
import com.framework.exception.DependencyInjectionException;
import com.framework.utils.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Conteneur de beans pour la gestion des dépendances
 */
public class BeanContainer {

    private Map<String, Object> singletons = new HashMap<>();
    private Map<String, BeanDefinition> definitions = new HashMap<>();
    private BeanInjector injector = new BeanInjector(this);
    private Set<String> beingCreated = new HashSet<>(); // Pour détecter les dépendances circulaires

    /**
     * Enregistre une définition de bean
     */
    public void registerDefinition(BeanDefinition definition) {
        definitions.put(definition.getId(), definition);
    }

    /**
     * Enregistre les définitions de beans
     */
    public void registerDefinitions(Map<String, BeanDefinition> defs) {
        definitions.putAll(defs);
    }

    /**
     * Obtient un bean par son ID
     */
    public Object getBean(String beanId) {
        if (beingCreated.contains(beanId)) {
            throw new DependencyInjectionException("Dépendance circulaire détectée pour le bean: " + beanId);
        }

        BeanDefinition definition = definitions.get(beanId);
        if (definition == null) {
            return null;
        }

        if (definition.isSingleton()) {
            Object instance = singletons.get(beanId);
            if (instance == null) {
                beingCreated.add(beanId);
                instance = createBean(definition);
                beingCreated.remove(beanId);
                singletons.put(beanId, instance);
            }
            return instance;
        } else {
            beingCreated.add(beanId);
            Object instance = createBean(definition);
            beingCreated.remove(beanId);
            return instance;
        }
    }

    /**
     * Obtient un bean par type (interface ou classe)
     */
    public Object getBean(Class<?> type) {
        return getBean(type.getSimpleName(), type);
    }

    /**
     * Obtient un bean par ID et type
     */
    public Object getBean(String beanId, Class<?> type) {
        Object bean = getBean(beanId);
        if (bean == null) {
            // Chercher par type si pas d'ID exact
            for (String id : definitions.keySet()) {
                Object candidate = getBean(id);
                if (candidate != null && type.isInstance(candidate)) {
                    return candidate;
                }
            }
        }
        return bean;
    }

    /**
     * Crée une instance d'un bean
     */
    private Object createBean(BeanDefinition definition) {
        try {
            Object instance;
            Class<?> clazz = definition.getBeanClass();
            InjectionMode mode = definition.getInjectionMode();

            if (mode == InjectionMode.CONSTRUCTOR) {
                instance = createBeanWithConstructor(clazz, definition);
            } else {
                instance = createBeanDefault(clazz);
            }

            // Injecter les propriétés si nécessaire
            if (mode != InjectionMode.CONSTRUCTOR || mode == null) {
                injector.injectByDefinition(instance, definition);
            }

            return instance;
        } catch (Exception e) {
            throw new DependencyInjectionException(
                "Erreur lors de la création du bean: " + definition.getId(), e
            );
        }
    }

    /**
     * Crée un bean avec injection par constructeur
     */
    private Object createBeanWithConstructor(Class<?> clazz, BeanDefinition definition) 
            throws InvocationTargetException, IllegalAccessException, InstantiationException {
        
        Object[] constructorArgs = injector.resolveConstructorArgs(definition);
        Constructor<?> constructor = null;

        // Chercher le constructeur correspondant
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            if (c.getParameterCount() == constructorArgs.length) {
                Class<?>[] paramTypes = c.getParameterTypes();
                boolean matches = true;
                for (int i = 0; i < paramTypes.length; i++) {
                    if (constructorArgs[i] != null && 
                        !paramTypes[i].isInstance(constructorArgs[i])) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    constructor = c;
                    break;
                }
            }
        }

        if (constructor == null) {
            constructor = clazz.getDeclaredConstructor();
        }

        constructor.setAccessible(true);
        return constructor.newInstance(constructorArgs.length > 0 ? constructorArgs : new Object[0]);
    }

    /**
     * Crée un bean avec le constructeur par défaut
     */
    private Object createBeanDefault(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        try {
            return ReflectionUtils.instantiate(clazz);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DependencyInjectionException("Erreur lors de l'instantiation de la classe: " + clazz.getName(), e);
        } catch (Exception e) {
            throw new DependencyInjectionException("Erreur lors de la création du bean", e);
        }
    }

    /**
     * Enregistre un bean singleton
     */
    public void registerSingleton(String beanId, Object instance) {
        singletons.put(beanId, instance);
    }

    /**
     * Enregistre un composant (classe annotée avec @Component)
     */
    public void registerComponent(Class<?> componentClass) {
        if (!componentClass.isAnnotationPresent(Component.class)) {
            throw new DependencyInjectionException(
                "La classe n'est pas annotée avec @Component: " + componentClass.getName()
            );
        }

        Component component = componentClass.getAnnotation(Component.class);
        String beanId = component.value();
        if (beanId == null || beanId.isEmpty()) {
            beanId = componentClass.getSimpleName();
        }

        BeanDefinition definition = new BeanDefinition(beanId, componentClass.getName());
        definition.setBeanClass(componentClass);
        definition.setInjectionMode(null); // Mode hybride avec annotations
        registerDefinition(definition);
    }

    /**
     * Scanne et enregistre tous les composants dans un package
     */
    public void scanComponents(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            
            // Note: Ceci est une implémentation simplifiée
            // Une véritable implémentation utiliserait la réflexion ou ClassPath scanning
        } catch (Exception e) {
            throw new DependencyInjectionException("Erreur lors du scan du package: " + packageName, e);
        }
    }

    /**
     * Obtient une list de tous les beans créés
     */
    public Collection<Object> getAllBeans() {
        return singletons.values();
    }

    /**
     * Vérifie si un bean existe
     */
    public boolean containsBean(String beanId) {
        return definitions.containsKey(beanId);
    }

    /**
     * Nettoie le conteneur
     */
    public void destroy() {
        singletons.clear();
        definitions.clear();
        beingCreated.clear();
    }
}
