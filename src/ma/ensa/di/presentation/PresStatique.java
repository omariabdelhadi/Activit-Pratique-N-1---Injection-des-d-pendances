package ma.ensa.di.presentation;

import ma.ensa.di.dao.DaoImpl;
import ma.ensa.di.metier.MetierImpl;

public class PresStatique {
    public static void main(String[] args) {
        DaoImpl dao = new DaoImpl();
        MetierImpl metier = new MetierImpl();
        metier.setDao(dao);
        System.out.println(metier.calcul());
    }
}
