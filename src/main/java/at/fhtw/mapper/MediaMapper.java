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
    private final IUserMapper userMapper;
    private final IRatingMapper ratingMapper;


    @Inject
    public MediaMapper(
            UserRepository userRepository,
            RatingRepository ratingRepository,
            IUserMapper userMapper,
            IRatingMapper ratingMapper
    ) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.userMapper = userMapper;
        this.ratingMapper = ratingMapper;
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
                media.getCreator().getId(),
                media.getRating()
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
                ratingRepository.findByMediaId(mediaDTO.getId()).stream().mapToInt(RatingEntity::getRating).average().orElse(0),
                ratingRepository.findByMediaId(mediaDTO.getId()).stream().map(ratingMapper::fromEntity).toList()
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
                ratingRepository.findByMediaId(mediaEntity.getId()).stream().mapToInt(RatingEntity::getRating).average().orElse(0),
                ratingRepository.findByMediaId(mediaEntity.getId()).stream().map(ratingMapper::fromEntity).toList()
        );
    }
}
