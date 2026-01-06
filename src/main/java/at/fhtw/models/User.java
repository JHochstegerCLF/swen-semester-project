package at.fhtw.models;

import at.fhtw.models.enums.Genre;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Genre favoriteGenre;
    private List<Media> favorites;
    private List<Rating> ratings;

    public User hashPassword() {
        this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        return this;
    }
}
