//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.2 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2026.02.19 à 09:25:56 PM WET 
//


package com.framework.xml.beans;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.framework.xml.beans package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.framework.xml.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Beans }
     * 
     */
    public Beans createBeans() {
        return new Beans();
    }

    /**
     * Create an instance of {@link Bean }
     * 
     */
    public Bean createBean() {
        return new Bean();
    }

    /**
     * Create an instance of {@link Constructor }
     * 
     */
    public Constructor createConstructor() {
        return new Constructor();
    }

    /**
     * Create an instance of {@link ConstructorArg }
     * 
     */
    public ConstructorArg createConstructorArg() {
        return new ConstructorArg();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

}
