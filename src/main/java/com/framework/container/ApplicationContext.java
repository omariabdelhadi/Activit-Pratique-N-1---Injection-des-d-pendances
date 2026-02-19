package com.framework.container;

import com.framework.xml.XmlParser;
import java.util.Map;

/**
 * Contexte applicatif principal - point d'entrée du framework
 * Gère le chargement des configurations et la création du container
 */
public class ApplicationContext {

    private BeanContainer container;
    private String configFile;

    /**
     * Crée un contexte basé sur un fichier de configuration XML
     */
    public ApplicationContext(String xmlConfigFile) {
        this.configFile = xmlConfigFile;
        this.container = new BeanContainer();
        initFromXml();
    }

    /**
     * Crée un contexte vide
     */
    public ApplicationContext() {
        this.container = new BeanContainer();
    }

    /**
     * Charge la configuration à partir d'un fichier XML
     */
    private void initFromXml() {
        try {
            Map<String, BeanDefinition> definitions = XmlParser.parseXml(configFile);
            container.registerDefinitions(definitions);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement du fichier de configuration: " + configFile, e);
        }
    }

    /**
     * Charge une configuration XML depuis une ressource (classpath)
     */
    public void loadXmlFromResource(String resourcePath) {
        Map<String, BeanDefinition> definitions = XmlParser.parseXmlFromResource(resourcePath);
        container.registerDefinitions(definitions);
    }

    /**
     * Charge une configuration XML depuis un fichier
     */
    public void loadXmlFromFile(String filePath) {
        Map<String, BeanDefinition> definitions = XmlParser.parseXml(filePath);
        container.registerDefinitions(definitions);
    }

    /**
     * Obtient un bean par son ID
     */
    public Object getBean(String beanId) {
        return container.getBean(beanId);
    }

    /**
     * Obtient un bean par son type
     */
    public <T> T getBean(Class<T> type) {
        Object bean = container.getBean(type);
        if (bean != null) {
            return (T) bean;
        }
        return null;
    }

    /**
     * Obtient un bean par son ID et type
     */
    public <T> T getBean(String beanId, Class<T> type) {
        Object bean = container.getBean(beanId, type);
        if (bean != null) {
            return (T) bean;
        }
        return null;
    }

    /**
     * Enregistre une définition de bean
     */
    public void registerBeanDefinition(BeanDefinition definition) {
        container.registerDefinition(definition);
    }

    /**
     * Enregistre un composant annoté
     */
    public void registerComponent(Class<?> componentClass) {
        container.registerComponent(componentClass);
    }

    /**
     * Enregistre un singleton
     */
    public void registerSingleton(String beanId, Object instance) {
        container.registerSingleton(beanId, instance);
    }

    /**
     * Obtient le container
     */
    public BeanContainer getContainer() {
        return container;
    }

    /**
     * Vérifie si un bean existe
     */
    public boolean containsBean(String beanId) {
        return container.containsBean(beanId);
    }

    /**
     * Ferme le contexte et libère les ressources
     */
    public void close() {
        container.destroy();
    }
}
