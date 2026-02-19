package com.framework.utils;

import java.lang.reflect.*;
import java.util.*;

/**
 * Utilitaires pour les opérations de réflexion
 */
public class ReflectionUtils {

    /**
     * Obtient toutes les annotations d'un type particulier d'une classe
     */
    public static <T> List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<T> annotationClass) {
        List<Field> result = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent((Class<? extends java.lang.annotation.Annotation>) annotationClass)) {
                result.add(field);
            }
        }
        return result;
    }

    /**
     * Obtient tous les setters d'une classe
     */
    public static List<Method> getSetters(Class<?> clazz) {
        List<Method> setters = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("set") && 
                method.getParameterCount() == 1 &&
                method.getReturnType() == void.class) {
                setters.add(method);
            }
        }
        return setters;
    }

    /**
     * Obtient tous les setters annotés avec une annotation donnée
     */
    public static <T> List<Method> getSettersWithAnnotation(Class<?> clazz, Class<T> annotationClass) {
        List<Method> setters = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("set") && 
                method.getParameterCount() == 1 &&
                method.getReturnType() == void.class &&
                method.isAnnotationPresent((Class<? extends java.lang.annotation.Annotation>) annotationClass)) {
                setters.add(method);
            }
        }
        return setters;
    }

    /**
     * Obtient tous les constructeurs annotés avec une annotation donnée
     */
    public static <T> List<Constructor<?>> getConstructorsWithAnnotation(Class<?> clazz, Class<T> annotationClass) {
        List<Constructor<?>> constructors = new ArrayList<>();
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent((Class<? extends java.lang.annotation.Annotation>) annotationClass)) {
                constructors.add(constructor);
            }
        }
        return constructors;
    }

    /**
     * Définit une valeur sur un field (même s'il est private)
     */
    public static void setFieldValue(Object obj, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * Obtient une valeur d'un field (même s'il est private)
     */
    public static Object getFieldValue(Object obj, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * Invoque une méthode sur un objet (même si elle est private)
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        return method.invoke(obj, args);
    }

    /**
     * Crée une instance d'une classe utilisant le constructeur par défaut
     */
    public static Object instantiate(Class<?> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    /**
     * Crée une instance avec des paramètres de constructeur
     */
    public static Object instantiate(Class<?> clazz, Object... args) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }
        Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
        constructor.setAccessible(true);
        return constructor.newInstance(args);
    }

    /**
     * Obtient le nom de la propriété à partir du nom du setter
     * Ex: "setName" -> "name"
     */
    public static String getPropertyNameFromSetter(String setterName) {
        if (setterName.startsWith("set") && setterName.length() > 3) {
            String prop = setterName.substring(3);
            return Character.toLowerCase(prop.charAt(0)) + prop.substring(1);
        }
        return null;
    }

    /**
     * Trouve un champ par son nom
     */
    public static Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * Convertit une valeur String vers le type cible
     */
    public static Object convertValue(String value, Class<?> targetType) {
        if (value == null) return null;
        
        if (targetType == String.class) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        } else if (targetType == float.class || targetType == Float.class) {
            return Float.parseFloat(value);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }
}
