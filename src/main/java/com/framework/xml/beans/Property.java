package com.framework.xml.beans;

import javax.xml.bind.annotation.*;

/**
 * Représente une propriété d'un bean à injecter via setter ou field
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Property {
    
    @XmlAttribute(required = true)
    private String name;
    
    @XmlAttribute
    private String value;
    
    @XmlAttribute
    private String ref;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public boolean isRef() {
        return ref != null && !ref.isEmpty();
    }
}
