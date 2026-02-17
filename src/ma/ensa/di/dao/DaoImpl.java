package ma.ensa.di.dao;

import org.springframework.stereotype.Component;

@Component  // Spring va créer un bean pour cette classe
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        return 20;
    }
}
