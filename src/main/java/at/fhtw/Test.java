package at.fhtw;

import at.fhtw.models.Media;
import at.fhtw.orm.Orm;
import at.fhtw.persistence.DBConnector;

public class Test {
    public static void main(String[] args) {
        DBConnector connector = new DBConnector();
        Orm<Media> mediaOrm = new Orm<>(Media.class, connector);
        System.out.println(mediaOrm.getAll());
    }
}
