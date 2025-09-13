package at.fhtw.models;

import java.sql.Timestamp;

public class Rating {
    private int id;
    private User creator;
    private Media media;
    private int rating;
    private String comment;
    private Timestamp timestamp;
}
