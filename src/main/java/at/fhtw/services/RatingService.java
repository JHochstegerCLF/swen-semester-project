package at.fhtw.services;

import at.fhtw.converter.JsonConverter;
import at.fhtw.mapper.LikeMapper;
import at.fhtw.mapper.RatingMapper;
import at.fhtw.models.dtos.LikeDTO;
import at.fhtw.models.dtos.RatingDTO;
import at.fhtw.models.entities.RatingEntity;
import at.fhtw.persistence.LikeRepository;
import at.fhtw.persistence.RatingRepository;
import at.fhtw.persistence.UserRepository;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Response;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(onConstructor_ = @Inject)
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final UserRepository userRepository;


    public Response addRating(RatingDTO rating) {
        rating.setTimestamp(LocalDateTime.now());
        if (ratingRepository.create(ratingMapper.toEntity(ratingMapper.fromDTO(rating))) != -1) {
            return new Response(
                    HttpStatus.CREATED,
                    ContentType.PLAIN_TEXT,
                    "Rating submitted"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response updateRating(RatingDTO rating, int userId) {
        RatingEntity ratingEntity = ratingRepository.findById(rating.getId());
        if (ratingEntity == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Rating not found"
            );
        }
        if (ratingEntity.getCreatorId() != userId) {
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.PLAIN_TEXT,
                    "You are not the creator of this rating"
            );
        }
        rating.setTimestamp(ratingEntity.getTimestamp());
        rating.setMediaId(ratingEntity.getMediaId());
        rating.setCreatorId(ratingEntity.getCreatorId());
        rating.setConfirmed(ratingEntity.isConfirmed());

        if (ratingRepository.update(rating.getId(), ratingMapper.toEntity(ratingMapper.fromDTO(rating))) != null) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Rating updated"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response deleteRating(int ratingId, int userId) {
        RatingEntity ratingEntity = ratingRepository.findById(ratingId);
        if (ratingEntity == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Rating not found"
            );
        }
        if (ratingEntity.getCreatorId() != userId) {
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.PLAIN_TEXT,
                    "You are not the creator of this rating"
            );
        }
        likeRepository.deleteByRatingId(ratingId);
        if (ratingRepository.delete(ratingId)) {
            return new Response(
                    HttpStatus.NO_CONTENT,
                    ContentType.PLAIN_TEXT,
                    "Rating deleted"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response confirmRating(int ratingId, int userId) {
        RatingEntity ratingEntity = ratingRepository.findById(ratingId);
        if (ratingEntity == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Rating not found"
            );
        }
        if (ratingEntity.getCreatorId() != userId) {
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.PLAIN_TEXT,
                    "You are not the creator of this rating"
            );
        }
        ratingEntity.setConfirmed(true);
        if (ratingRepository.update(ratingEntity.getId(), ratingEntity) != null) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Comment confirmed"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response addLike(LikeDTO likeDTO) {
        if (ratingRepository.findById(likeDTO.getRating()) == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Rating not found"
            );
        }
        if (likeRepository.create(likeMapper.toEntity(likeMapper.fromDTO(likeDTO))) != -1) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Rating liked"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response getRatings(int userId) {
        if (userRepository.findById(userId) == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "User not found"
            );
        }
        List<RatingDTO> ratings = ratingRepository.findByUserId(userId).stream().map(ratingMapper::fromEntity).map(ratingMapper::toDTO).toList();
        JsonConverter<List> jsonConverter = new JsonConverter<>(List.class);
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                jsonConverter.serialize(ratings)
        );

    }
}
