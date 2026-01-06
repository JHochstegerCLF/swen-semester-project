package at.fhtw.persistence;

import at.fhtw.models.entities.FavoriteEntity;
import at.fhtw.orm.Orm;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class FavoriteRepository {

    private final Orm<FavoriteEntity> favoriteORM;

    @Inject
    public FavoriteRepository(Orm<FavoriteEntity> favoriteORM) {
        this.favoriteORM = favoriteORM;
    }

    public List<FavoriteEntity> findByUserId(int userId) {
        return favoriteORM.getByField("user", userId);
    }

    public List<FavoriteEntity> findByMediaId(int mediaId) {
        return favoriteORM.getByField("media", mediaId);
    }

    public int create(FavoriteEntity favoriteEntity) {
        return favoriteORM.persistEntity(favoriteEntity);
    }

    public boolean delete(int id) {
        return favoriteORM.delete(id);
    }
}
