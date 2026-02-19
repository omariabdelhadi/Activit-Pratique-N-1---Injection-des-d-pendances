package com.framework.annotations;

import java.lang.annotation.*;

/**
 * Annotation pour qualifier les dépendances injectées
 * Utile quand plusieurs implémentations du même type existent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Documented
public @interface Qualifier {
    /**
     * Le nom du bean qualifié
     */
    String value();
}
