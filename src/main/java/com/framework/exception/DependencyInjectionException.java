package com.framework.exception;

/**
 * Exception levée lors d'erreurs d'injection de dépendances
 */
public class DependencyInjectionException extends RuntimeException {
    
    public DependencyInjectionException(String message) {
        super(message);
    }

    public DependencyInjectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DependencyInjectionException(Throwable cause) {
        super(cause);
    }
}
