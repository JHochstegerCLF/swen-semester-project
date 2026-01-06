package at.fhtw.models;

import at.fhtw.orm.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Key
    private int id;
    private User user;
    private Rating rating;
}
