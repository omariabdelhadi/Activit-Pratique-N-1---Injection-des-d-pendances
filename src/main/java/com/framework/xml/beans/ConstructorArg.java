package com.framework.xml.beans;

import javax.xml.bind.annotation.*;

/**
 * Représente un argument du constructeur
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ConstructorArg {
    
    @XmlAttribute
    private String type;
    
    @XmlAttribute
    private String value;
    
    @XmlAttribute
    private String ref;
    
    @XmlAttribute
    private Integer index;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public boolean isRef() {
        return ref != null && !ref.isEmpty();
    }
}
