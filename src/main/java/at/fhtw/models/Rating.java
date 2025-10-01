package at.fhtw.models;

import java.sql.Timestamp;

public class Rating {
    private int id;
    private User creator;
    private IMedia media;
    private int rating;
    private String comment;
    private Timestamp timestamp;
}
