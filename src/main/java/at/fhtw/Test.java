package at.fhtw;

import at.fhtw.models.Media;
import at.fhtw.orm.Orm;
import at.fhtw.persistence.DBConnector;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        DBConnector connector = new DBConnector();
        Orm<Media> mediaOrm = new Orm<>(Media.class, connector);
        System.out.println(mediaOrm.getAll());
    }
}
