package at.fhtw;

import at.fhtw.mapper.*;
import at.fhtw.models.entities.*;
import at.fhtw.orm.Orm;
import at.fhtw.persistence.DBConnector;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(IUserMapper.class).to(UserMapper.class).in(Singleton.class);
        bind(IMediaMapper.class).to(MediaMapper.class).in(Singleton.class);
        bind(IRatingMapper.class).to(RatingMapper.class).in(Singleton.class);
    }

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

    @Provides
    @Singleton
    public Orm<LikeEntity> provideLikeOrm(DBConnector connector) {
        return new Orm<>(LikeEntity.class, connector);
    }

    @Provides
    @Singleton
    public Orm<FavoriteEntity> provideFavoriteOrm(DBConnector connector) {
        return new Orm<>(FavoriteEntity.class, connector);
    }
}
