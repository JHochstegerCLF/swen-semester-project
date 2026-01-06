package at.fhtw.models.entities;

import at.fhtw.orm.Entity;
import at.fhtw.orm.Key;
import at.fhtw.orm.Param;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "rating")
public class RatingEntity {
    @Key
    private int id;
    @Param(name = "creator")
    private int creatorId;
    @Param(name = "media")
    private int mediaId;
    private int rating;
    private String comment;
    private LocalDateTime timestamp;
    private boolean confirmed;
}
