package ma.ensa.di.metier;

import ma.ensa.di.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component  // Spring va créer un bean pour cette classe
public class MetierImpl implements IMetier {

    @Autowired   // Spring injecte automatiquement un IDao
    private IDao dao;

    @Override
    public double calcul() {
        return dao.getData() * 2;
    }
}
