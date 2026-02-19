package com.framework.xml;

import com.framework.container.BeanDefinition;
import com.framework.container.InjectionMode;
import com.framework.exception.BeanDefinitionException;
import com.framework.xml.beans.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * Parseur pour les fichiers de configuration XML
 */
public class XmlParser {

    /**
     * Parse un fichier XML et retourne les définitions des beans
     */
    public static Map<String, BeanDefinition> parseXml(String xmlFilePath) {
        try {
            File file = new File(xmlFilePath);
            return parseXml(file);
        } catch (Exception e) {
            throw new BeanDefinitionException("Erreur lors de la lecture du fichier XML: " + xmlFilePath, e);
        }
    }

    /**
     * Parse un fichier XML à partir d'une ressource
     */
    public static Map<String, BeanDefinition> parseXmlFromResource(String resourcePath) {
        try {
            InputStream inputStream = XmlParser.class.getResourceAsStream(resourcePath);
            if (inputStream == null) {
                throw new BeanDefinitionException("Ressource non trouvée: " + resourcePath);
            }
            return parseXmlFromStream(inputStream);
        } catch (Exception e) {
            throw new BeanDefinitionException("Erreur lors de la lecture de la ressource XML: " + resourcePath, e);
        }
    }

    /**
     * Parse un fichier XML
     */
    public static Map<String, BeanDefinition> parseXml(File xmlFile) {
        try {
            JAXBContext context = JAXBContext.newInstance(Beans.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Beans beansConfig = (Beans) unmarshaller.unmarshal(xmlFile);
            return extractBeanDefinitions(beansConfig);
        } catch (JAXBException e) {
            throw new BeanDefinitionException("Erreur lors du parsing du fichier XML: " + xmlFile.getName(), e);
        }
    }

    /**
     * Parse un fichier XML à partir d'un InputStream
     */
    public static Map<String, BeanDefinition> parseXmlFromStream(InputStream inputStream) {
        try {
            JAXBContext context = JAXBContext.newInstance(Beans.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Beans beansConfig = (Beans) unmarshaller.unmarshal(inputStream);
            return extractBeanDefinitions(beansConfig);
        } catch (JAXBException e) {
            throw new BeanDefinitionException("Erreur lors du parsing du fichier XML", e);
        }
    }

    /**
     * Extrait les définitions des beans à partir de la configuration JAXB
     */
    private static Map<String, BeanDefinition> extractBeanDefinitions(Beans beansConfig) {
        Map<String, BeanDefinition> definitions = new HashMap<>();

        for (Bean bean : beansConfig.getBeans()) {
            String beanId = bean.getId();
            String beanClassName = bean.getClassName();

            if (beanId == null || beanId.isEmpty()) {
                throw new BeanDefinitionException("Un bean doit avoir un ID");
            }
            if (beanClassName == null || beanClassName.isEmpty()) {
                throw new BeanDefinitionException("Un bean doit avoir une classe (attribut 'class')");
            }

            BeanDefinition definition = new BeanDefinition(beanId, beanClassName);

            // Charger la classe
            try {
                definition.setBeanClass(Class.forName(beanClassName));
            } catch (ClassNotFoundException e) {
                throw new BeanDefinitionException("Classe non trouvée: " + beanClassName, e);
            }

            // Traiter les paramètres du constructeur
            if (bean.getConstructor() != null) {
                definition.setInjectionMode(InjectionMode.CONSTRUCTOR);
                for (ConstructorArg arg : bean.getConstructor().getArgs()) {
                    String value = arg.getValue();
                    String ref = arg.getRef();
                    Class<?> type = arg.getType() != null ? resolveType(arg.getType()) : null;
                    
                    definition.addConstructorArg(value, ref, type);
                }
            } else if (!bean.getProperties().isEmpty()) {
                // Déterminer le mode d'injection selon le contexte
                // Par défaut: SETTER si les propriétés sont définies
                definition.setInjectionMode(InjectionMode.SETTER);
                
                // Traiter les propriétés
                for (Property property : bean.getProperties()) {
                    String propName = property.getName();
                    String value = property.getValue();
                    String ref = property.getRef();
                    
                    define.addProperty(propName, value, ref, null);
                }
            }

            definitions.put(beanId, definition);
        }

        return definitions;
    }

    /**
     * Résout un type à partir de son nom
     */
    private static Class<?> resolveType(String typeName) {
        try {
            switch (typeName) {
                case "int":
                case "java.lang.Integer":
                    return int.class;
                case "long":
                case "java.lang.Long":
                    return long.class;
                case "double":
                case "java.lang.Double":
                    return double.class;
                case "float":
                case "java.lang.Float":
                    return float.class;
                case "boolean":
                case "java.lang.Boolean":
                    return boolean.class;
                case "java.lang.String":
                    return String.class;
                default:
                    return Class.forName(typeName);
            }
        } catch (ClassNotFoundException e) {
            throw new BeanDefinitionException("Type non trouvé: " + typeName, e);
        }
    }
}
