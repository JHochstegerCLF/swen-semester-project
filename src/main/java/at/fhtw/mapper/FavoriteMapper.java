package at.fhtw.mapper;

import at.fhtw.models.Favorite;
import at.fhtw.models.dtos.FavoriteDTO;
import at.fhtw.models.entities.FavoriteEntity;
import at.fhtw.persistence.MediaRepository;
import at.fhtw.persistence.UserRepository;
import com.google.inject.Inject;

public class FavoriteMapper {
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final IUserMapper userMapper;
    private final IMediaMapper mediaMapper;


    @Inject
    public FavoriteMapper(
            UserRepository userRepository,
            MediaRepository mediaRepository,
            IUserMapper userMapper,
            IMediaMapper mediaMapper
    ) {
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        this.userMapper = userMapper;
        this.mediaMapper = mediaMapper;
    }

    public FavoriteDTO toDTO(Favorite favorite) {
        return new FavoriteDTO(
                favorite.getId(),
                favorite.getUser().getId(),
                favorite.getMedia().getId()
        );
    }

    public Favorite fromDTO(FavoriteDTO favoriteDTO) {
        return new Favorite(
                favoriteDTO.getId(),
                userMapper.fromEntity(userRepository.findById(favoriteDTO.getUser())),
                mediaMapper.fromEntity(mediaRepository.findById(favoriteDTO.getMedia()))
        );
    }

    public FavoriteEntity toEntity(Favorite favorite) {
        return new FavoriteEntity(
                favorite.getId(),
                favorite.getUser().getId(),
                favorite.getMedia().getId()
        );
    }

    public Favorite fromEntity(FavoriteEntity favoriteEntity) {
        return new Favorite(
                favoriteEntity.getId(),
                userMapper.fromEntity(userRepository.findById(favoriteEntity.getUser())),
                mediaMapper.fromEntity(mediaRepository.findById(favoriteEntity.getMedia()))
        );
    }
}
