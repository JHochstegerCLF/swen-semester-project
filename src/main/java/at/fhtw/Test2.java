package at.fhtw;

import at.fhtw.models.entities.MediaEntity;
import at.fhtw.models.entities.RatingEntity;
import at.fhtw.models.entities.UserEntity;
import at.fhtw.orm.Orm;
import at.fhtw.persistence.DBConnector;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Test2 {
    public static void main(String[] args) throws SQLException {
        DBConnector connector = new DBConnector();

        Orm<MediaEntity> mediaOrm = new Orm<>(MediaEntity.class, connector);
        Orm<UserEntity> userOrm = new Orm<>(UserEntity.class, connector);
        Orm<RatingEntity> ratingOrm = new Orm<>(RatingEntity.class, connector);

        UserEntity user = new UserEntity(0, "John Doe", "john.doe@example.com", "password", 2);

        System.out.println("Inserting entities...");
        int userId = userOrm.persistEntity(user);
        System.out.println(userId);
        MediaEntity media = new MediaEntity(0, "Test", "TestDesc",2, 2023, List.of(1, 2), 18, userId);
        int mediaId = mediaOrm.persistEntity(media);
        System.out.println(mediaId);
        RatingEntity rating = new RatingEntity(0, mediaId, userId, 5, "Test", LocalDateTime.now());
        int ratingId = ratingOrm.persistEntity(rating);
        System.out.println(ratingId);

        System.out.println("Getting by field");
        System.out.println(userOrm.getByField("favoriteGenre", 2));
        System.out.println(mediaOrm.getByField("mediaType", 2));
        System.out.println(ratingOrm.getByField("mediaId", 7));
    }
}
