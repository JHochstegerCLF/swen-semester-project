package at.fhtw.mapper;

import at.fhtw.models.Rating;
import at.fhtw.models.dtos.RatingDTO;
import at.fhtw.models.entities.RatingEntity;
import at.fhtw.persistence.MediaRepository;
import at.fhtw.persistence.UserRepository;
import com.google.inject.Inject;

public class RatingMapper implements IRatingMapper {
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;


    @Inject
    public RatingMapper(
            UserRepository userRepository,
            MediaRepository mediaRepository
    ) {
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
    }

    public RatingDTO toDTO(Rating rating) {
        return new RatingDTO(
                rating.getId(),
                rating.getCreator() != null ? rating.getCreator().getId() : 0,
                rating.getMedia() != null ? rating.getMedia().getId() : 0,
                rating.getRating(),
                rating.getComment(),
                rating.getTimestamp(),
                rating.isConfirmed()
        );
    }

    public Rating fromDTO(RatingDTO ratingDTO) {
        at.fhtw.models.entities.UserEntity userEntity = userRepository.findById(ratingDTO.getCreatorId());
        at.fhtw.models.User user = null;
        if (userEntity != null) {
            user = new at.fhtw.models.User(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getEmail(), userEntity.getFavoriteGenre() != null ? at.fhtw.models.enums.Genre.values()[userEntity.getFavoriteGenre()] : null, new java.util.ArrayList<>(), new java.util.ArrayList<>());
        }

        at.fhtw.models.entities.MediaEntity mediaEntity = mediaRepository.findById(ratingDTO.getMediaId());
        at.fhtw.models.Media media = null;
        if (mediaEntity != null) {
            media = new at.fhtw.models.Media(mediaEntity.getId(), mediaEntity.getTitle(), mediaEntity.getDescription(), mediaEntity.getMediaType() != null ? at.fhtw.models.enums.MediaType.values()[mediaEntity.getMediaType()] : null, mediaEntity.getReleaseYear(), mediaEntity.getGenres() != null ? mediaEntity.getGenres().stream().map(g -> at.fhtw.models.enums.Genre.values()[g]).toList() : null, mediaEntity.getAgeRestriction(), null, 0.0, new java.util.ArrayList<>());
        }

        return new Rating(
                ratingDTO.getId(),
                user,
                media,
                ratingDTO.getRating(),
                ratingDTO.getComment(),
                ratingDTO.getTimestamp(),
                ratingDTO.isConfirmed()
        );
    }

    public RatingEntity toEntity(Rating rating) {
        return new RatingEntity(
                rating.getId(),
                rating.getCreator() != null ? rating.getCreator().getId() : 0,
                rating.getMedia() != null ? rating.getMedia().getId() : 0,
                rating.getRating(),
                rating.getComment(),
                rating.getTimestamp(),
                rating.isConfirmed()
        );
    }

    public Rating fromEntity(RatingEntity ratingEntity) {
        at.fhtw.models.entities.UserEntity userEnt = userRepository.findById(ratingEntity.getCreatorId());
        at.fhtw.models.User user = null;
        if (userEnt != null) {
            user = new at.fhtw.models.User(userEnt.getId(), userEnt.getUsername(), userEnt.getPassword(), userEnt.getEmail(), userEnt.getFavoriteGenre() != null ? at.fhtw.models.enums.Genre.values()[userEnt.getFavoriteGenre()] : null, new java.util.ArrayList<>(), new java.util.ArrayList<>());
        }

        at.fhtw.models.entities.MediaEntity medEnt = mediaRepository.findById(ratingEntity.getMediaId());
        at.fhtw.models.Media media = null;
        if (medEnt != null) {
            media = new at.fhtw.models.Media(medEnt.getId(), medEnt.getTitle(), medEnt.getDescription(), medEnt.getMediaType() != null ? at.fhtw.models.enums.MediaType.values()[medEnt.getMediaType()] : null, medEnt.getReleaseYear(), medEnt.getGenres() != null ? medEnt.getGenres().stream().map(g -> at.fhtw.models.enums.Genre.values()[g]).toList() : null, medEnt.getAgeRestriction(), null, 0.0, new java.util.ArrayList<>());
        }

        return new Rating(
                ratingEntity.getId(),
                user,
                media,
                ratingEntity.getRating(),
                ratingEntity.getComment(),
                ratingEntity.getTimestamp(),
                ratingEntity.isConfirmed()
        );
    }
}
