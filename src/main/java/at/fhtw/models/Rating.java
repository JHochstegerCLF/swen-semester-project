package at.fhtw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    private int id;
    private User creator;
    private Media media;
    private int rating;
    private String comment;
    private LocalDateTime timestamp;
}
