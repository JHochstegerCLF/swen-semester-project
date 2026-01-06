package at.fhtw.persistence;

import at.fhtw.models.entities.LikeEntity;
import at.fhtw.orm.Orm;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class LikeRepository {

    private final Orm<LikeEntity> likeORM;

    @Inject
    public LikeRepository(Orm<LikeEntity> likeORM) {
        this.likeORM = likeORM;
    }

    public List<LikeEntity> findByUserId(int userId) {
        return likeORM.getByField("user", userId);
    }

    public List<LikeEntity> findByRatingId(int ratingId) {
        return likeORM.getByField("rating", ratingId);
    }

    public int create(LikeEntity likeEntity) {
        return likeORM.persistEntity(likeEntity);
    }

    public boolean delete(int id) {
        return likeORM.delete(id);
    }

    public void deleteByRatingId(int ratingId) {
        List<LikeEntity> likes = findByRatingId(ratingId);
        if (likes != null) {
            likes.forEach(l -> delete(l.getId()));
        }
    }
}
