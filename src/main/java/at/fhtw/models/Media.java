package at.fhtw.models;

import at.fhtw.models.enums.Genre;
import at.fhtw.models.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    private int id;
    private String title;
    private String description;
    private MediaType mediaType;
    private int releaseYear;
    private List<Genre> genres;
    private int ageRestriction;
    private User creator;
    private int rating = 0;

    public void update(Media media) {
        this.title = media.getTitle();
        this.description = media.getDescription();
        this.mediaType = media.getMediaType();
        this.releaseYear = media.getReleaseYear();
        this.genres = media.getGenres();
        this.ageRestriction = media.getAgeRestriction();
        this.creator = media.getCreator();
        this.rating = media.getRating();
    }
}
