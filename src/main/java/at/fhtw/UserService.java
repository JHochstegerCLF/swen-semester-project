package at.fhtw;


import at.fhtw.models.User;

public class UserService {
    // Business Logic
    public boolean checkUser(User user) {
       return true;
    }

    // Service Logic
    public User[] getAllUser() {
        return new User[0];
    }

    // Persistence
    public User getUser(int id) {
        return null;
    }

    public void saveUser(User user) {
        System.out.println("User saved");
    }
}
