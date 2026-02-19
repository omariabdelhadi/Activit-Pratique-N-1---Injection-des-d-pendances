package com.framework.container;

/**
 * Énumération des modes d'injection de dépendances supportés
 */
public enum InjectionMode {
    /**
     * Injection via le constructeur
     */
    CONSTRUCTOR,
    
    /**
     * Injection via les setters
     */
    SETTER,
    
    /**
     * Injection via les fields (accès direct aux attributs)
     */
    FIELD
}
