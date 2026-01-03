package at.fhtw.models;

import at.fhtw.models.enums.Genre;
import com.google.common.hash.Hashing;
import lombok.*;

import java.nio.charset.StandardCharsets;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Genre favoriteGenre;

    public User hashPassword() {
        this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        return this;
    }
}
