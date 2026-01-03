package at.fhtw.models.dtos;

import at.fhtw.models.enums.Genre;
import at.fhtw.models.enums.MediaType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    private int id;
    private String title;
    private String description;
    private String mediaType;
    private int releaseYear;
    private List<String> genres;
    private int ageRestriction;
    @JsonProperty("creator")
    private int creatorId;
}
