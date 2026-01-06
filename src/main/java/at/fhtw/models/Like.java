package at.fhtw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    private int id;
    private User user;
    private Rating rating;
}
