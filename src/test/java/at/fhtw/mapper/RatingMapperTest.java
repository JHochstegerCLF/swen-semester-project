package at.fhtw.mapper;

import at.fhtw.models.Media;
import at.fhtw.models.Rating;
import at.fhtw.models.User;
import at.fhtw.models.dtos.RatingDTO;
import at.fhtw.models.entities.RatingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import at.fhtw.persistence.UserRepository;
import at.fhtw.persistence.MediaRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RatingMapperTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private MediaRepository mediaRepository;
    @Mock
    private IUserMapper userMapper;
    @Mock
    private IMediaMapper mediaMapper;

    private RatingMapper ratingMapper;

    @BeforeEach
    void setUp() {
        ratingMapper = new RatingMapper(userRepository, mediaRepository, userMapper, mediaMapper);
    }

    @Test
    void testToDTO() {
        User creator = new User(); creator.setId(1);
        Media media = new Media(); media.setId(2);
        LocalDateTime now = LocalDateTime.now();
        Rating rating = new Rating(10, creator, media, 5, "Good", now, true);

        RatingDTO dto = ratingMapper.toDTO(rating);

        assertEquals(10, dto.getId());
        assertEquals(1, dto.getCreatorId());
        assertEquals(2, dto.getMediaId());
        assertEquals(5, dto.getRating());
        assertEquals("Good", dto.getComment());
        assertTrue(dto.isConfirmed());
    }

    @Test
    void testToEntity() {
        User creator = new User(); creator.setId(1);
        Media media = new Media(); media.setId(2);
        LocalDateTime now = LocalDateTime.now();
        Rating rating = new Rating(10, creator, media, 5, "Good", now, true);

        RatingEntity entity = ratingMapper.toEntity(rating);

        assertEquals(10, entity.getId());
        assertEquals(1, entity.getCreatorId());
        assertEquals(2, entity.getMediaId());
        assertEquals("Good", entity.getComment());
    }

    @Test
    void testFromDTO() {
        LocalDateTime now = LocalDateTime.now();
        RatingDTO dto = new RatingDTO(10, 1, 2, 5, "Good", now, true);
        User creator = new User(); creator.setId(1);
        Media media = new Media(); media.setId(2);

        when(userRepository.findById(1)).thenReturn(null);
        when(mediaRepository.findById(2)).thenReturn(null);
        when(userMapper.fromEntity(null)).thenReturn(creator);
        when(mediaMapper.fromEntity(null)).thenReturn(media);

        Rating rating = ratingMapper.fromDTO(dto);

        assertEquals(10, rating.getId());
        assertEquals(1, rating.getCreator().getId());
        assertEquals(2, rating.getMedia().getId());
    }

    @Test
    void testFromEntity() {
        LocalDateTime now = LocalDateTime.now();
        RatingEntity entity = new RatingEntity(10, 1, 2, 5, "Good", now, true);
        User creator = new User(); creator.setId(1);
        Media media = new Media(); media.setId(2);

        when(userRepository.findById(1)).thenReturn(null);
        when(mediaRepository.findById(2)).thenReturn(null);
        when(userMapper.fromEntity(null)).thenReturn(creator);
        when(mediaMapper.fromEntity(null)).thenReturn(media);

        Rating rating = ratingMapper.fromEntity(entity);

        assertEquals(10, rating.getId());
        assertEquals(5, rating.getRating());
    }
}
