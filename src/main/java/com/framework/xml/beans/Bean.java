package com.framework.xml.beans;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Définition d'un bean dans le fichier XML
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Bean {
    
    @XmlAttribute(required = true)
    private String id;
    
    @XmlAttribute(required = true)
    private String class_;
    
    @XmlAttribute(name = "class")
    private String className;
    
    @XmlElement(name = "constructor")
    private ConstructorDef constructor;
    
    @XmlElement(name = "property")
    private List<Property> properties = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        // Utiliser l'attribut "class" si disponible, sinon "class_"
        return className != null ? className : class_;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    public void setClass_(String class_) {
        this.class_ = class_;
    }

    public ConstructorDef getConstructor() {
        return constructor;
    }

    public void setConstructor(ConstructorDef constructor) {
        this.constructor = constructor;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }
}
