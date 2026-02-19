package com.framework.xml.beans;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Racine du document XML : élément <beans>
 */
@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class Beans {
    
    @XmlElement(name = "bean")
    private List<Bean> beans = new ArrayList<>();

    public List<Bean> getBeans() {
        return beans;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }

    public void addBean(Bean bean) {
        this.beans.add(bean);
    }
}
