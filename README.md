# Injection de dépendances

## 🎯 Objectif
Implémenter le principe de l’injection des dépendances en Java afin de réduire le couplage entre les composants d’une application, puis concevoir un mini Framework d’injection des dépendances inspiré de Spring IOC.

---

## 🧩 Partie 1 : Injection des dépendances en Java

### Étapes réalisées :
- Création des interfaces `IDao` et `IMetier`
- Création des implémentations correspondantes
- Injection des dépendances :
  - Par instanciation statique
  - Par instanciation dynamique (Reflection)
  - En utilisant le Framework Spring :
    - Version XML
    - Version Annotations

### Résultat :
Le programme calcule une valeur basée sur les données fournies par la couche DAO, en utilisant un couplage faible entre les composants.

---

## 🧪 Partie 2 : Mini Framework d’injection des dépendances

### Objectif :
Développer un mini Framework permettant de gérer l’injection des dépendances entre les différents composants d’une application, de manière similaire au framework Spring IOC.

### Fonctionnalités du mini Framework :
Le Framework permet l’injection des dépendances selon les mécanismes suivants :

#### 1️⃣ Configuration par fichier XML
- Injection des dépendances à travers un fichier XML de configuration
- Utilisation du principe de mapping Objet-XML (OXM) avec Jax Binding

#### 2️⃣ Configuration par annotations
- Détection des composants via des annotations personnalisées
- Injection automatique des dépendances sans fichier XML

#### 3️⃣ Types d’injection supportés
- a) Injection par constructeur  
- b) Injection par Setter  
- c) Injection par attribut (Field)

---

## 📂 Architecture du projet
