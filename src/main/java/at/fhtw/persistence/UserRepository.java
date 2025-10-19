package at.fhtw.persistence;

import at.fhtw.models.User;

import java.util.*;

public class UserRepository {
    private Set<User> users = new HashSet<>();

    public boolean createUser(User user) { //TODO Move logic to service
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
}
