package com.framework.annotations;

import java.lang.annotation.*;

/**
 * Annotation pour l'injection automatique des dépendances
 * Peut être appliquée sur :
 * - Constructeurs et leurs paramètres
 * - Setters
 * - Attributs (fields)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface Autowired {
    /**
     * Indique si l'injection est obligatoire.
     * Si true et que la dépendance n'existe pas, une exception est levée
     */
    boolean required() default true;
}
