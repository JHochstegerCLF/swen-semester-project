package at.fhtw.models.entities;

import at.fhtw.orm.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "like")
public class LikeEntity {
    private int id;
    private int user;
    private int rating;
}
