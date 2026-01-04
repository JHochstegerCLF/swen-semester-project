package at.fhtw.mapper;

import at.fhtw.models.Rating;
import at.fhtw.models.dtos.RatingDTO;
import at.fhtw.models.entities.RatingEntity;
import at.fhtw.persistence.MediaRepository;
import at.fhtw.persistence.UserRepository;
import jakarta.inject.Inject;

public class RatingMapper {
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final UserMapper userMapper;
    private final MediaMapper mediaMapper;


    @Inject
    public RatingMapper(
            UserRepository userRepository,
            MediaRepository mediaRepository,
            UserMapper userMapper,
            MediaMapper mediaMapper
    ) {
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        this.userMapper = userMapper;
        this.mediaMapper = mediaMapper;
    }

    public RatingDTO toDTO(Rating rating) {
        return new RatingDTO(
                rating.getId(),
                rating.getCreator().getId(),
                rating.getMedia().getId(),
                rating.getRating(),
                rating.getComment(),
                rating.getTimestamp()
        );
    }

    public Rating fromDTO(RatingDTO ratingDTO) {
        return new Rating(
                ratingDTO.getId(),
                userMapper.fromEntity(userRepository.findById(ratingDTO.getCreatorId())),
                mediaMapper.fromEntity(mediaRepository.findById(ratingDTO.getMediaId())),
                ratingDTO.getRating(),
                ratingDTO.getComment(),
                ratingDTO.getTimestamp()
        );
    }

    public RatingEntity toEntity(Rating rating) {
        return new RatingEntity(
                rating.getId(),
                rating.getCreator().getId(),
                rating.getMedia().getId(),
                rating.getRating(),
                rating.getComment(),
                rating.getTimestamp()
        );
    }

    public Rating fromEntity(RatingEntity ratingEntity) {
        return new Rating(
                ratingEntity.getId(),
                userMapper.fromEntity(userRepository.findById(ratingEntity.getCreatorId())),
                mediaMapper.fromEntity(mediaRepository.findById(ratingEntity.getMediaId())),
                ratingEntity.getRating(),
                ratingEntity.getComment(),
                ratingEntity.getTimestamp()
        );
    }
}
