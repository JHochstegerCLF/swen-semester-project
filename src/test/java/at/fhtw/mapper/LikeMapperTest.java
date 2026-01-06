package at.fhtw.mapper;

import at.fhtw.models.Like;
import at.fhtw.models.Rating;
import at.fhtw.models.User;
import at.fhtw.models.dtos.LikeDTO;
import at.fhtw.models.entities.LikeEntity;
import at.fhtw.persistence.RatingRepository;
import at.fhtw.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeMapperTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private IUserMapper userMapper;
    @Mock
    private RatingMapper ratingMapper;

    private LikeMapper likeMapper;

    @BeforeEach
    void setUp() {
        likeMapper = new LikeMapper(userRepository, ratingRepository, userMapper, ratingMapper);
    }

    @Test
    void testToDTO() {
        User user = new User();
        user.setId(1);
        Rating rating = new Rating();
        rating.setId(100);
        Like like = new Like(5, user, rating);

        LikeDTO dto = likeMapper.toDTO(like);

        assertEquals(5, dto.getId());
        assertEquals(1, dto.getUser());
        assertEquals(100, dto.getRating());
    }

    @Test
    void testToEntity() {
        User user = new User();
        user.setId(1);
        Rating rating = new Rating();
        rating.setId(100);
        Like like = new Like(5, user, rating);

        LikeEntity entity = likeMapper.toEntity(like);

        assertEquals(5, entity.getId());
        assertEquals(1, entity.getUser());
        assertEquals(100, entity.getRating());
    }

    @Test
    void testFromDTO() {
        LikeDTO dto = new LikeDTO(5, 1, 100);
        User user = new User();
        user.setId(1);
        Rating rating = new Rating();
        rating.setId(100);

        when(userRepository.findById(1)).thenReturn(null);
        when(ratingRepository.findById(100)).thenReturn(null);
        when(userMapper.fromEntity(null)).thenReturn(user);
        when(ratingMapper.fromEntity(null)).thenReturn(rating);

        Like like = likeMapper.fromDTO(dto);

        assertEquals(5, like.getId());
        assertEquals(1, like.getUser().getId());
        assertEquals(100, like.getRating().getId());
    }

    @Test
    void testFromEntity() {
        LikeEntity entity = new LikeEntity(5, 1, 100);
        User user = new User();
        user.setId(1);
        Rating rating = new Rating();
        rating.setId(100);

        when(userRepository.findById(1)).thenReturn(null);
        when(ratingRepository.findById(100)).thenReturn(null);
        when(userMapper.fromEntity(null)).thenReturn(user);
        when(ratingMapper.fromEntity(null)).thenReturn(rating);

        Like like = likeMapper.fromEntity(entity);

        assertEquals(5, like.getId());
        assertEquals(1, like.getUser().getId());
        assertEquals(100, like.getRating().getId());
    }
}
