package at.fhtw.persistence;

import at.fhtw.models.entities.RatingEntity;
import at.fhtw.orm.Orm;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class RatingRepository {

    private final Orm<RatingEntity> ratingOrm;

    @Inject
    public RatingRepository(Orm<RatingEntity> ratingOrm) {
        this.ratingOrm = ratingOrm;
    }

    public List<RatingEntity> findAll() {
        return ratingOrm.getAll();
    }

    public RatingEntity findById(int id) {
        return ratingOrm.getById(id);
    }

    public List<RatingEntity> findByMediaId(int mediaId) {
        List<RatingEntity> ratings = ratingOrm.getByField("mediaId", mediaId);
        return ratings != null ? ratings : List.of();
    }

    public List<RatingEntity> findByUserId(int userId) {
        List<RatingEntity> ratings = ratingOrm.getByField("creatorId", userId);
        return ratings != null ? ratings : List.of();
    }

    public int create(RatingEntity rating) {
        return ratingOrm.persistEntity(rating);
    }

    public RatingEntity update(int id, RatingEntity rating) {
        return ratingOrm.update(id, rating);
    }

    public boolean delete(int id) {
        return ratingOrm.delete(id);
    }
}
