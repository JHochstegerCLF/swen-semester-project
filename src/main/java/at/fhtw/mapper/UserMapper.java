package at.fhtw.mapper;

import at.fhtw.models.Favorite;
import at.fhtw.models.User;
import at.fhtw.models.dtos.UserDTO;
import at.fhtw.models.entities.UserEntity;
import at.fhtw.models.enums.Genre;
import at.fhtw.persistence.FavoriteRepository;
import at.fhtw.persistence.RatingRepository;
import com.google.inject.Inject;

public class UserMapper implements IUserMapper {
    private final IMediaMapper mediaMapper;
    private final RatingRepository ratingRepository;
    private final IRatingMapper ratingMapper;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;


    @Inject
    public UserMapper(
            IMediaMapper mediaMapper,
            RatingRepository ratingRepository,
            IRatingMapper ratingMapper,
            FavoriteRepository favoriteRepository,
            FavoriteMapper favoriteMapper
    ) {
        this.mediaMapper = mediaMapper;
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFavoriteGenre() != null ? user.getFavoriteGenre().toString() : null,
                user.getFavorites().stream().map(mediaMapper::toDTO).toList()
        );
    }

    public User fromDTO(UserDTO userDTO) {
        return new User(userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getFavoriteGenre() != null ? Genre.valueOf(userDTO.getFavoriteGenre()) : null,
                userDTO.getFavorites() != null ? userDTO.getFavorites().stream().map(mediaMapper::fromDTO).toList() : new java.util.ArrayList<>(),
                ratingRepository.findByUserId(userDTO.getId()).stream().map(ratingMapper::fromEntity).toList()
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFavoriteGenre() != null ? user.getFavoriteGenre().ordinal() : null
        );
    }

    public User fromEntity(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.getFavoriteGenre() != null ? Genre.values()[userEntity.getFavoriteGenre()] : null,
                favoriteRepository.findByUserId(userEntity.getId()).stream().map(favoriteMapper::fromEntity).map(Favorite::getMedia).toList(),
                ratingRepository.findByUserId(userEntity.getId()).stream().map(ratingMapper::fromEntity).toList()
        );
    }
}
