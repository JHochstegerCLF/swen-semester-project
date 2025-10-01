package at.fhtw.models;

import at.fhtw.models.enums.MediaType;

public class Movie implements IMedia {
    private int id;
    private String title;
    private String description;
    private final MediaType mediaType = MediaType.MOVIE;
    private int release_year;
    private int age_restriction;
    private int creator_id;

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public MediaType getMediaType() {
        return this.mediaType;
    }

    @Override
    public int getReleaseYear() {
        return this.release_year;
    }

    @Override
    public int getAgeRestriction() {
        return this.age_restriction;
    }

    @Override
    public User getCreator() {
        return null;
    }
}
