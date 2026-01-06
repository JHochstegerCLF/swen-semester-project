package at.fhtw.mapper;

import at.fhtw.models.Media;
import at.fhtw.models.User;
import at.fhtw.models.dtos.UserDTO;
import at.fhtw.models.entities.UserEntity;
import at.fhtw.models.enums.Genre;
import at.fhtw.persistence.MediaRepository;
import at.fhtw.persistence.RatingRepository;
import com.google.inject.Inject;

public class UserMapper implements IUserMapper {
    private final MediaRepository mediaRepository;
    private final IMediaMapper mediaMapper;
    private final RatingRepository ratingRepository;
    private final IRatingMapper ratingMapper;


    @Inject
    public UserMapper(
            IMediaMapper mediaMapper,
            MediaRepository mediaRepository,
            RatingRepository ratingRepository,
            IRatingMapper ratingMapper
    ) {
        this.mediaMapper = mediaMapper;
        this.mediaRepository = mediaRepository;
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFavoriteGenre().toString(),
                user.getFavorites().stream().map(mediaMapper::toDTO).toList()
        );
    }

    public User fromDTO(UserDTO userDTO) {
        return new User(userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                Genre.valueOf(userDTO.getFavoriteGenre()),
                userDTO.getFavorites().stream().map(mediaMapper::fromDTO).toList(),
                ratingRepository.findByUserId(userDTO.getId()).stream().map(ratingMapper::fromEntity).toList()
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFavoriteGenre().ordinal(),
                user.getFavorites().stream().map(Media::getId).toList());
    }

    public User fromEntity(UserEntity userEntity) {
        return new User(userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                Genre.values()[userEntity.getFavoriteGenre()],
                userEntity.getFavorites().stream().map(mediaRepository::findById).map(mediaMapper::fromEntity).toList(),
                ratingRepository.findByUserId(userEntity.getId()).stream().map(ratingMapper::fromEntity).toList()
        );
    }
}
