package at.fhtw.persistence;

import at.fhtw.models.User;
import com.google.inject.Singleton;

import java.util.*;

@Singleton
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public boolean createUser(User user) {
        return users.add(user);
    }

    public User getUserbyId(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    public User getUserByName(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    public boolean updateUser(User user) {
        return true;
    }

    public List<User> getUsers() {
        return users;
    }
}
