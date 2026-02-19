package com.framework.xml.beans;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente les paramètres du constructeur à injecter
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ConstructorDef {
    
    @XmlElement(name = "arg")
    private List<ConstructorArg> args = new ArrayList<>();

    public List<ConstructorArg> getArgs() {
        return args;
    }

    public void setArgs(List<ConstructorArg> args) {
        this.args = args;
    }

    public void addArg(ConstructorArg arg) {
        this.args.add(arg);
    }
}
