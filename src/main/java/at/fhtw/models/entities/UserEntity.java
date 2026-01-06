package at.fhtw.models.entities;

import at.fhtw.orm.Entity;
import at.fhtw.orm.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class UserEntity {
    @Key
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Integer favoriteGenre;
}
