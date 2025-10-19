package at.fhtw.models.dtos;

import at.fhtw.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
    private String username;
    private String password;

    public User toUser() {
        return new User(null, username, password, null, null);
    }
}
