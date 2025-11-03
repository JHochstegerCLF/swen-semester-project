package at.fhtw.models;

import at.fhtw.models.enums.MediaType;
import at.fhtw.orm.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Media {
    private int id;
    private String title;
    private String description;
    private MediaType mediaType;
    private int releaseYear;
    private List<String> genres;
    private int ageRestriction;
    private int creatorId;
    private int rating = 0;

    public Media update(Media media) {
        this.title = media.getTitle();
        this.description = media.getDescription();
        this.mediaType = media.getMediaType();
        this.releaseYear = media.getReleaseYear();
        this.genres = media.getGenres();
        this.ageRestriction = media.getAgeRestriction();
        return this;
    }
}
