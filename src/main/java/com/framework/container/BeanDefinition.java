package com.framework.container;

import java.util.*;

/**
 * Définition d'un bean gérée par le container
 */
public class BeanDefinition {
    
    private String id;
    private String className;
    private Class<?> beanClass;
    private Object instance;
    private boolean singleton = true;
    
    // Pour l'injection par constructor
    private List<String> constructorArgRefs = new ArrayList<>();
    private List<String> constructorArgValues = new ArrayList<>();
    private List<Class<?>> constructorArgTypes = new ArrayList<>();
    
    // Pour l'injection par properties (setter ou field)
    private Map<String, String> propertyRefs = new HashMap<>();
    private Map<String, String> propertyValues = new HashMap<>();
    private Map<String, Class<?>> propertyTypes = new HashMap<>();
    
    // Mode d'injection
    private InjectionMode injectionMode;

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public List<String> getConstructorArgRefs() {
        return constructorArgRefs;
    }

    public List<String> getConstructorArgValues() {
        return constructorArgValues;
    }

    public List<Class<?>> getConstructorArgTypes() {
        return constructorArgTypes;
    }

    public void addConstructorArg(String value, String ref, Class<?> type) {
        constructorArgValues.add(value);
        constructorArgRefs.add(ref);
        constructorArgTypes.add(type);
    }

    public Map<String, String> getPropertyRefs() {
        return propertyRefs;
    }

    public Map<String, String> getPropertyValues() {
        return propertyValues;
    }

    public Map<String, Class<?>> getPropertyTypes() {
        return propertyTypes;
    }

    public void addProperty(String name, String value, String ref, Class<?> type) {
        propertyValues.put(name, value);
        propertyRefs.put(name, ref);
        propertyTypes.put(name, type);
    }

    public InjectionMode getInjectionMode() {
        return injectionMode;
    }

    public void setInjectionMode(InjectionMode injectionMode) {
        this.injectionMode = injectionMode;
    }
}
