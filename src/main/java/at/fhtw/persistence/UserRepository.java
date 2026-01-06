package at.fhtw.persistence;

import at.fhtw.models.entities.UserEntity;
import at.fhtw.orm.Orm;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class UserRepository {

    private final Orm<UserEntity> userORM;

    @Inject
    public UserRepository(Orm<UserEntity> userORM) {
        this.userORM = userORM;
    }

    public List<UserEntity> findAll() {
        return userORM.getAll();
    }

    public UserEntity findById(int id) {
        return userORM.getById(id);
    }

    public UserEntity findByUsername(String username) {
        return userORM.getByField("username", username).getFirst();
    }

    public int create(UserEntity user) {
        return userORM.persistEntity(user);
    }

    public UserEntity update(int id, UserEntity user) {
        return userORM.update(id, user);
    }

    public boolean delete(int id) {
        return userORM.delete(id);
    }
}
