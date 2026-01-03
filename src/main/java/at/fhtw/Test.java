package at.fhtw;

import at.fhtw.models.Media;
import at.fhtw.models.Rating;
import at.fhtw.models.User;
import at.fhtw.models.entities.MediaEntity;
import at.fhtw.models.entities.RatingEntity;
import at.fhtw.models.entities.UserEntity;
import at.fhtw.orm.Orm;
import at.fhtw.persistence.DBConnector;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Test {
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

        System.out.println("Getting entities...");
        System.out.println(userOrm.getAll());
        System.out.println(mediaOrm.getAll());
        System.out.println(ratingOrm.getAll());

        System.out.println("Getting by ID...");
        System.out.println(userOrm.getById(userId));
        System.out.println(mediaOrm.getById(mediaId));
        System.out.println(ratingOrm.getById(ratingId));

        System.out.println("Updating entities...");
        user.setUsername("Jane Doe");
        System.out.println(userOrm.update(userId, user));
        media.setTitle("Test2");
        System.out.println(mediaOrm.update(mediaId, media));
        rating.setRating(4);
        System.out.println(ratingOrm.update(ratingId, rating));

        System.out.println("Deleting entities...");
        System.out.println(userOrm.getAll().size());
        System.out.println(mediaOrm.getAll().size());
        System.out.println(ratingOrm.getAll().size());

        ratingOrm.delete(ratingId);
        mediaOrm.delete(mediaId);
        userOrm.delete(userId);

        System.out.println("Getting entities...");
        System.out.println(userOrm.getAll().size());
        System.out.println(mediaOrm.getAll().size());
        System.out.println(ratingOrm.getAll().size());
    }
}
