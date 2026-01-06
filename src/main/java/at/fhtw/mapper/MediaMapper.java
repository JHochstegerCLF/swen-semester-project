package at.fhtw.mapper;

import at.fhtw.models.Media;
import at.fhtw.models.dtos.MediaDTO;
import at.fhtw.models.entities.MediaEntity;
import at.fhtw.models.entities.RatingEntity;
import at.fhtw.models.enums.Genre;
import at.fhtw.models.enums.MediaType;
import at.fhtw.persistence.RatingRepository;
import at.fhtw.persistence.UserRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MediaMapper implements IMediaMapper {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final IRatingMapper ratingMapper;


    @Inject
    public MediaMapper(
            UserRepository userRepository,
            RatingRepository ratingRepository,
            IRatingMapper ratingMapper
    ) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    public MediaDTO toDTO(Media media) {
        return new MediaDTO(
                media.getId(),
                media.getTitle(),
                media.getDescription(),
                media.getMediaType() != null ? media.getMediaType().toString() : null,
                media.getReleaseYear(),
                media.getGenres() != null ? media.getGenres().stream().map(String::valueOf).toList() : null,
                media.getAgeRestriction(),
                media.getCreator() != null ? media.getCreator().getId() : 0,
                media.getRating()
        );
    }

    public Media fromDTO(MediaDTO mediaDTO) {
        at.fhtw.models.entities.UserEntity creatorEntity = userRepository.findById(mediaDTO.getCreatorId());
        at.fhtw.models.User creator = null;
        if (creatorEntity != null) {
            creator = new at.fhtw.models.User(
                    creatorEntity.getId(),
                    creatorEntity.getUsername(),
                    creatorEntity.getPassword(),
                    creatorEntity.getEmail(),
                    creatorEntity.getFavoriteGenre() != null ? Genre.values()[creatorEntity.getFavoriteGenre()] : null,
                    new java.util.ArrayList<>(),
                    new java.util.ArrayList<>()
            );
        }

        return new Media(
                mediaDTO.getId(),
                mediaDTO.getTitle(),
                mediaDTO.getDescription(),
                mediaDTO.getMediaType() != null ? MediaType.valueOf(mediaDTO.getMediaType().toUpperCase()) : null,
                mediaDTO.getReleaseYear(),
                mediaDTO.getGenres() != null ? mediaDTO.getGenres().stream().map(Genre::valueOf).toList() : null,
                mediaDTO.getAgeRestriction(),
                creator,
                ratingRepository.findByMediaId(mediaDTO.getId()).stream().mapToInt(RatingEntity::getRating).average().orElse(0),
                ratingRepository.findByMediaId(mediaDTO.getId()).stream().map(ratingMapper::fromEntity).toList()
        );
    }

    public MediaEntity toEntity(Media media) {
        return new MediaEntity(
                media.getId(),
                media.getTitle(),
                media.getDescription(),
                media.getMediaType() != null ? media.getMediaType().ordinal() : null,
                media.getReleaseYear(),
                media.getGenres() != null ? media.getGenres().stream().map(Genre::ordinal).toList() : null,
                media.getAgeRestriction(),
                media.getCreator() != null ? media.getCreator().getId() : 0
        );
    }

    public Media fromEntity(MediaEntity mediaEntity) {
        at.fhtw.models.entities.UserEntity creatorEntity = userRepository.findById(mediaEntity.getCreatorId());
        at.fhtw.models.User creator = null;
        if (creatorEntity != null) {
            creator = new at.fhtw.models.User(
                    creatorEntity.getId(),
                    creatorEntity.getUsername(),
                    creatorEntity.getPassword(),
                    creatorEntity.getEmail(),
                    creatorEntity.getFavoriteGenre() != null ? Genre.values()[creatorEntity.getFavoriteGenre()] : null,
                    new java.util.ArrayList<>(),
                    new java.util.ArrayList<>()
            );
        }

        return new Media(
                mediaEntity.getId(),
                mediaEntity.getTitle(),
                mediaEntity.getDescription(),
                mediaEntity.getMediaType() != null ? MediaType.values()[mediaEntity.getMediaType()] : null,
                mediaEntity.getReleaseYear(),
                mediaEntity.getGenres() != null ? mediaEntity.getGenres().stream().map(g -> Genre.values()[g]).toList() : null,
                mediaEntity.getAgeRestriction(),
                creator,
                ratingRepository.findByMediaId(mediaEntity.getId()).stream().mapToInt(RatingEntity::getRating).average().orElse(0),
                ratingRepository.findByMediaId(mediaEntity.getId()).stream().map(ratingMapper::fromEntity).toList()
        );
    }
}
