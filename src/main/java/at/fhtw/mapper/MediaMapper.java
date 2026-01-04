package at.fhtw.mapper;

import at.fhtw.models.Media;
import at.fhtw.models.dtos.MediaDTO;
import at.fhtw.models.entities.MediaEntity;
import at.fhtw.models.entities.RatingEntity;
import at.fhtw.models.enums.Genre;
import at.fhtw.models.enums.MediaType;
import at.fhtw.persistence.RatingRepository;
import at.fhtw.persistence.UserRepository;
import jakarta.inject.Inject;

public class MediaMapper {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final UserMapper userMapper;


    @Inject
    public MediaMapper(
            UserRepository userRepository,
            RatingRepository ratingRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.userMapper = userMapper;
    }

    public MediaDTO toDTO(Media media) {
        return new MediaDTO(
                media.getId(),
                media.getTitle(),
                media.getDescription(),
                media.getMediaType().toString(),
                media.getReleaseYear(),
                media.getGenres().stream().map(String::valueOf).toList(),
                media.getAgeRestriction(),
                media.getCreator().getId()
        );
    }

    public Media fromDTO(MediaDTO mediaDTO) {
        return new Media(
                mediaDTO.getId(),
                mediaDTO.getTitle(),
                mediaDTO.getDescription(),
                MediaType.valueOf(mediaDTO.getMediaType()),
                mediaDTO.getReleaseYear(),
                mediaDTO.getGenres().stream().map(Genre::valueOf).toList(),
                mediaDTO.getAgeRestriction(),
                userMapper.fromEntity(userRepository.findById(mediaDTO.getCreatorId())),
                (int) ratingRepository.findByMediaId(mediaDTO.getId()).stream().mapToInt(RatingEntity::getRating).average().orElse(0)
        );
    }

    public MediaEntity toEntity(Media media) {
        return new MediaEntity(
                media.getId(),
                media.getTitle(),
                media.getDescription(),
                media.getMediaType().ordinal(),
                media.getReleaseYear(),
                media.getGenres().stream().map(Genre::ordinal).toList(),
                media.getAgeRestriction(),
                media.getCreator().getId()
        );
    }

    public Media fromEntity(MediaEntity mediaEntity) {
        return new Media(
                mediaEntity.getId(),
                mediaEntity.getTitle(),
                mediaEntity.getDescription(),
                MediaType.values()[mediaEntity.getMediaType()],
                mediaEntity.getReleaseYear(),
                mediaEntity.getGenres().stream().map(g -> Genre.values()[g]).toList(),
                mediaEntity.getAgeRestriction(),
                userMapper.fromEntity(userRepository.findById(mediaEntity.getCreatorId())),
                (int) ratingRepository.findByMediaId(mediaEntity.getId()).stream().mapToInt(RatingEntity::getRating).average().orElse(0)
        );
    }
}
