package com.framework.container;

import com.framework.annotations.Autowired;
import com.framework.annotations.Qualifier;
import com.framework.exception.DependencyInjectionException;
import com.framework.utils.ReflectionUtils;

import java.lang.reflect.*;
import java.util.*;

/**
 * Injector de dépendances - gère l'injection via constructeur, setter ou field
 */
public class BeanInjector {

    private BeanContainer container;

    public BeanInjector(BeanContainer container) {
        this.container = container;
    }

    /**
     * Injecte les dépendances dans un objet
     */
    public void inject(Object instance, Class<?> clazz, InjectionMode mode) {
        if (mode == null) {
            // Mode hybride: chercher les annotations
            injectAnnotations(instance, clazz);
        } else {
            switch (mode) {
                case CONSTRUCTOR:
                    // L'injection du constructeur a déjà été faite lors de la création
                    break;
                case SETTER:
                    injectBySetter(instance, clazz);
                    break;
                case FIELD:
                    injectByField(instance, clazz);
                    break;
            }
        }
    }

    /**
     * Injection via les annotations
     */
    private void injectAnnotations(Object instance, Class<?> clazz) {
        // Injection sur les fields annotés avec @Autowired
        injectByFieldAnnotation(instance, clazz);
        
        // Injection par setters annotés avec @Autowired
        injectBySetterAnnotation(instance, clazz);
    }

    /**
     * Injection via les fields annoté avec @Autowired
     */
    private void injectByFieldAnnotation(Object instance, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Autowired autowired = field.getAnnotation(Autowired.class);
                String beanId = getBeanId(field);
                Object dependency = container.getBean(beanId, field.getType());
                
                if (dependency == null && autowired.required()) {
                    throw new DependencyInjectionException(
                        "Impossible d'injecter la dépendance pour le field: " + field.getName() +
                        " de la classe: " + clazz.getName()
                    );
                }
                
                if (dependency != null) {
                    try {
                        ReflectionUtils.setFieldValue(instance, field, dependency);
                    } catch (IllegalAccessException e) {
                        throw new DependencyInjectionException(
                            "Erreur lors de l'injection du field: " + field.getName(), e
                        );
                    }
                }
            }
        }
    }

    /**
     * Injection via les setters annotés avec @Autowired
     */
    private void injectBySetterAnnotation(Object instance, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowired.class) && isSetter(method)) {
                Autowired autowired = method.getAnnotation(Autowired.class);
                
                Class<?> paramType = method.getParameterTypes()[0];
                String beanId = getBeanIdFromParameter(method);
                Object dependency = container.getBean(beanId, paramType);
                
                if (dependency == null && autowired.required()) {
                    throw new DependencyInjectionException(
                        "Impossible d'injecter la dépendance pour le setter: " + method.getName() +
                        " de la classe: " + clazz.getName()
                    );
                }
                
                if (dependency != null) {
                    try {
                        ReflectionUtils.invokeMethod(instance, method, dependency);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new DependencyInjectionException(
                            "Erreur lors de l'injection du setter: " + method.getName(), e
                        );
                    }
                }
            }
        }
    }

    /**
     * Injection via les fields (accès direct)
     */
    private void injectByField(Object instance, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                String beanId = getBeanId(field);
                Object dependency = container.getBean(beanId, field.getType());
                
                if (dependency != null) {
                    try {
                        ReflectionUtils.setFieldValue(instance, field, dependency);
                    } catch (IllegalAccessException e) {
                        throw new DependencyInjectionException(
                            "Erreur lors de l'injection du field: " + field.getName(), e
                        );
                    }
                }
            }
        }
    }

    /**
     * Injection via les setters
     */
    private void injectBySetter(Object instance, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (isSetter(method) && method.isAnnotationPresent(Autowired.class)) {
                Class<?> paramType = method.getParameterTypes()[0];
                String beanId = getBeanIdFromParameter(method);
                Object dependency = container.getBean(beanId, paramType);
                
                if (dependency != null) {
                    try {
                        ReflectionUtils.invokeMethod(instance, method, dependency);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new DependencyInjectionException(
                            "Erreur lors de l'injection du setter: " + method.getName(), e
                        );
                    }
                }
            }
        }
    }

    /**
     * Injection selon la définition du bean (XML)
     */
    public void injectByDefinition(Object instance, BeanDefinition definition) {
        Class<?> clazz = definition.getBeanClass();
        
        InjectionMode mode = definition.getInjectionMode();
        
        if (mode == InjectionMode.SETTER) {
            injectPropertiesBySetter(instance, clazz, definition);
        } else if (mode == InjectionMode.FIELD) {
            injectPropertiesByField(instance, clazz, definition);
        } else if (mode == InjectionMode.CONSTRUCTOR) {
            // Déjà injecté lors de la création
        }
    }

    /**
     * Injecte les propriétés via les setters
     */
    private void injectPropertiesBySetter(Object instance, Class<?> clazz, BeanDefinition definition) {
        Map<String, String> propertyRefs = definition.getPropertyRefs();
        Map<String, String> propertyValues = definition.getPropertyValues();

        for (String propName : propertyValues.keySet()) {
            String setterName = "set" + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
            
            try {
                Method setter = findSetter(clazz, setterName);
                if (setter != null) {
                    Class<?> paramType = setter.getParameterTypes()[0];
                    Object value = resolvePropertyValue(propName, paramType, propertyValues, propertyRefs);
                    
                    if (value != null) {
                        ReflectionUtils.invokeMethod(instance, setter, value);
                    }
                }
            } catch (Exception e) {
                throw new DependencyInjectionException(
                    "Erreur lors de l'injection de la propriété: " + propName, e
                );
            }
        }
    }

    /**
     * Injecte les propriétés via les fields
     */
    private void injectPropertiesByField(Object instance, Class<?> clazz, BeanDefinition definition) {
        Map<String, String> propertyRefs = definition.getPropertyRefs();
        Map<String, String> propertyValues = definition.getPropertyValues();

        for (String propName : propertyValues.keySet()) {
            Field field = ReflectionUtils.findField(clazz, propName);
            if (field != null) {
                try {
                    Object value = resolvePropertyValue(propName, field.getType(), propertyValues, propertyRefs);
                    if (value != null) {
                        ReflectionUtils.setFieldValue(instance, field, value);
                    }
                } catch (Exception e) {
                    throw new DependencyInjectionException(
                        "Erreur lors de l'injection du field: " + propName, e
                    );
                }
            }
        }
    }

    /**
     * Résout la valeur d'une propriété (value ou ref)
     */
    private Object resolvePropertyValue(String propName, Class<?> targetType, 
                                       Map<String, String> values, Map<String, String> refs) {
        String ref = refs.get(propName);
        if (ref != null && !ref.isEmpty()) {
            return container.getBean(ref);
        }
        
        String value = values.get(propName);
        if (value != null && !value.isEmpty()) {
            return ReflectionUtils.convertValue(value, targetType);
        }
        
        return null;
    }

    /**
     * Résout les arguments du constructeur
     */
    public Object[] resolveConstructorArgs(BeanDefinition definition) {
        List<String> values = definition.getConstructorArgValues();
        List<String> refs = definition.getConstructorArgRefs();
        List<Class<?>> types = definition.getConstructorArgTypes();
        
        Object[] args = new Object[values.size()];
        for (int i = 0; i < values.size(); i++) {
            String ref = refs.get(i);
            if (ref != null && !ref.isEmpty()) {
                args[i] = container.getBean(ref);
            } else {
                String value = values.get(i);
                Class<?> type = types.get(i);
                args[i] = ReflectionUtils.convertValue(value, type != null ? type : String.class);
            }
        }
        return args;
    }

    /**
     * Obtient l'ID du bean à injecter à partir d'une annotation @Qualifier ou par type
     */
    private String getBeanId(Field field) {
        if (field.isAnnotationPresent(Qualifier.class)) {
            return field.getAnnotation(Qualifier.class).value();
        }
        return field.getType().getSimpleName();
    }

    /**
     * Obtient l'ID du bean à injecter à partir de l'annotation @Qualifier sur le paramètre
     */
    private String getBeanIdFromParameter(Method method) {
        Parameter[] params = method.getParameters();
        if (params.length > 0 && params[0].isAnnotationPresent(Qualifier.class)) {
            return params[0].getAnnotation(Qualifier.class).value();
        }
        return params[0].getType().getSimpleName();
    }

    /**
     * Vérifie si une méthode est un setter
     */
    private boolean isSetter(Method method) {
        return method.getName().startsWith("set") &&
               method.getParameterCount() == 1 &&
               method.getReturnType() == void.class;
    }

    /**
     * Trouve un setter par son nom
     */
    private Method findSetter(Class<?> clazz, String setterName) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(setterName) && isSetter(method)) {
                return method;
            }
        }
        return null;
    }
}
