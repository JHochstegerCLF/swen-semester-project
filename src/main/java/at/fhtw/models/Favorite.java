package at.fhtw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    private int id;
    private User user;
    private Media media;
}
