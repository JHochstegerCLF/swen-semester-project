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
        List<FavoriteEntity> favorites = favoriteORM.getByField("user", userId);
        return favorites != null ? favorites : List.of();
    }

    public List<FavoriteEntity> findByMediaId(int mediaId) {
        List<FavoriteEntity> favorites = favoriteORM.getByField("media", mediaId);
        return favorites != null ? favorites : List.of();
    }

    public int create(FavoriteEntity favoriteEntity) {
        return favoriteORM.persistEntity(favoriteEntity);
    }

    public boolean delete(int id) {
        return favoriteORM.delete(id);
    }
}
