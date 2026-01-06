package at.fhtw.mapper;

import at.fhtw.models.Rating;
import at.fhtw.models.dtos.RatingDTO;
import at.fhtw.models.entities.RatingEntity;

public interface IRatingMapper {
    RatingDTO toDTO(Rating rating);

    Rating fromDTO(RatingDTO ratingDTO);

    RatingEntity toEntity(Rating rating);

    Rating fromEntity(RatingEntity ratingEntity);
}
