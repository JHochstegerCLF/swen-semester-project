package at.fhtw.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Rating {
    private int id;
    private User creator;
    private Media media;
    private int rating;
    private String comment;
    private Timestamp timestamp;
}
