package com.framework.annotations;

import java.lang.annotation.*;

/**
 * Annotation marqueur pour identifier les composants gérés par le framework
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Component {
    /**
     * Le nom (identifiant) du composant
     */
    String value() default "";
}
