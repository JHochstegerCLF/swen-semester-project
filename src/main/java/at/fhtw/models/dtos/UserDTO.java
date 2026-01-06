package at.fhtw.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String favoriteGenre;
    private List<MediaDTO> favorites;
}
