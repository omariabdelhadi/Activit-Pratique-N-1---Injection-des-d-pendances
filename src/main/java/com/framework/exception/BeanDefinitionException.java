package com.framework.exception;

/**
 * Exception levée lors d'erreurs de définition des beans
 */
public class BeanDefinitionException extends RuntimeException {
    
    public BeanDefinitionException(String message) {
        super(message);
    }

    public BeanDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanDefinitionException(Throwable cause) {
        super(cause);
    }
}
