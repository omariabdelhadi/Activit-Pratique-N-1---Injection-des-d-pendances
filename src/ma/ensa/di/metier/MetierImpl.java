package ma.ensa.di.metier;

import ma.ensa.di.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MetierImpl implements IMetier {

    @Autowired
    @Qualifier("daoImpl") // choisir DaoImpl, ou "daoImplV2" pour DaoImplV2
    private IDao dao;

    @Override
    public double calcul() {
        return dao.getData() * 2;
    }
}