package at.fhtw.mapper;

import at.fhtw.models.Like;
import at.fhtw.models.dtos.LikeDTO;
import at.fhtw.models.entities.LikeEntity;
import at.fhtw.persistence.RatingRepository;
import at.fhtw.persistence.UserRepository;
import com.google.inject.Inject;

public class LikeMapper {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final IUserMapper userMapper;
    private final RatingMapper ratingMapper;


    @Inject
    public LikeMapper(
            UserRepository userRepository,
            RatingRepository ratingRepository,
            IUserMapper userMapper,
            RatingMapper ratingMapper
    ) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.userMapper = userMapper;
        this.ratingMapper = ratingMapper;
    }

    public LikeDTO toDTO(Like like) {
        return new LikeDTO(
                like.getId(),
                like.getUser().getId(),
                like.getRating().getId()
        );
    }

    public Like fromDTO(LikeDTO likeDTO) {
        return new Like(
                likeDTO.getId(),
                userMapper.fromEntity(userRepository.findById(likeDTO.getUser())),
                ratingMapper.fromEntity(ratingRepository.findById(likeDTO.getRating()))
        );
    }

    public LikeEntity toEntity(Like like) {
        return new LikeEntity(
                like.getId(),
                like.getUser().getId(),
                like.getRating().getId()
        );
    }

    public Like fromEntity(LikeEntity likeEntity) {
        return new Like(
                likeEntity.getId(),
                userMapper.fromEntity(userRepository.findById(likeEntity.getUser())),
                ratingMapper.fromEntity(ratingRepository.findById(likeEntity.getRating()))
        );
    }
}
