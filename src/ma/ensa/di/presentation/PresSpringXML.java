package ma.ensa.di.presentation;

import ma.ensa.di.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresSpringXML {
    public static void main(String[] args) {
        // Charger le fichier XML
        ApplicationContext context =
            new ClassPathXmlApplicationContext("applicationContext.xml");

        // Récupérer le bean Metier
        IMetier metier = (IMetier) context.getBean("metier");

        // Exécuter le calcul
        System.out.println(metier.calcul());
    }
}
