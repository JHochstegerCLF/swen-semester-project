package at.fhtw;

import at.fhtw.models.entities.MediaEntity;
import at.fhtw.models.entities.RatingEntity;
import at.fhtw.models.entities.UserEntity;
import at.fhtw.orm.Orm;
import at.fhtw.persistence.DBConnector;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class Module extends AbstractModule {

    @Provides
    @Singleton
    public DBConnector provideDBConnector() {
        return new DBConnector();
    }

    @Provides
    @Singleton
    public Orm<UserEntity> provideUserOrm(DBConnector connector) {
        return new Orm<>(UserEntity.class, connector);
    }

    @Provides
    @Singleton
    public Orm<MediaEntity> provideMediaOrm(DBConnector connector) {
        return new Orm<>(MediaEntity.class, connector);
    }

    @Provides
    @Singleton
    public Orm<RatingEntity> provideRatingOrm(DBConnector connector) {
        return new Orm<>(RatingEntity.class, connector);
    }
}
