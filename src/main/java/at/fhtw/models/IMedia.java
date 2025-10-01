package at.fhtw.models;

import at.fhtw.models.enums.MediaType;

public interface IMedia {
    public String getTitle();
    public String getDescription();
    public MediaType getMediaType();
    public int getReleaseYear();
    public int getAgeRestriction();
    public User getCreator();
}
