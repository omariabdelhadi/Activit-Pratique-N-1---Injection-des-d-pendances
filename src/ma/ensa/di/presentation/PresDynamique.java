package ma.ensa.di.presentation;

import ma.ensa.di.dao.IDao;
import ma.ensa.di.metier.IMetier;
import java.io.File;
import java.util.Scanner;

public class PresDynamique {
    public static void main(String[] args) throws Exception {
        try (Scanner sc = new Scanner(new File("config.txt"))) {
            String daoClassName = sc.nextLine();
            String metierClassName = sc.nextLine();

            Class<?> cDao = Class.forName(daoClassName);
            IDao dao = (IDao) cDao.getDeclaredConstructor().newInstance();

            Class<?> cMetier = Class.forName(metierClassName);
            IMetier metier = (IMetier) cMetier.getDeclaredConstructor().newInstance();

            cMetier.getMethod("setDao", IDao.class).invoke(metier, dao);

            System.out.println(metier.calcul());
        }
    }
}
