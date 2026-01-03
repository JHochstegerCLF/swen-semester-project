package at.fhtw.models.entities;

import at.fhtw.models.Media;
import at.fhtw.models.enums.Genre;
import at.fhtw.models.enums.MediaType;
import at.fhtw.orm.Entity;
import at.fhtw.orm.Key;
import at.fhtw.orm.Param;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "media")
public class MediaEntity {
    @Key
    private int id;
    private String title;
    private String description;
    private Integer mediaType;
    private int releaseYear;
    private List<Integer> genres;
    private int ageRestriction;
    @Param(name = "creator")
    private int creatorId;
}
