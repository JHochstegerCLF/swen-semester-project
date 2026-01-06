package at.fhtw.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private int id;
    private int creatorId;
    private int mediaId;
    @JsonProperty("stars")
    private int rating;
    private String comment;
    private LocalDateTime timestamp;
    private boolean confirmed;
}
