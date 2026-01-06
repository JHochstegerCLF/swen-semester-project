package at.fhtw.mapper;

import at.fhtw.models.Media;
import at.fhtw.models.User;
import at.fhtw.models.dtos.MediaDTO;
import at.fhtw.models.entities.MediaEntity;
import at.fhtw.models.enums.Genre;
import at.fhtw.models.enums.MediaType;
import at.fhtw.persistence.RatingRepository;
import at.fhtw.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MediaMapperTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private IUserMapper userMapper;
    @Mock
    private IRatingMapper ratingMapper;

    private MediaMapper mediaMapper;

    @BeforeEach
    void setUp() {
        mediaMapper = new MediaMapper(userRepository, ratingRepository, userMapper, ratingMapper);
    }

    @Test
    void testToDTO() {
        User creator = new User();
        creator.setId(99);
        Media media = new Media(100, "Inception", "Dreams", MediaType.MOVIE, 2010, List.of(Genre.SCIENCE_FICTION), 12, creator, 9.0, Collections.emptyList());

        MediaDTO dto = mediaMapper.toDTO(media);

        assertEquals("Inception", dto.getTitle());
        assertEquals("MOVIE", dto.getMediaType());
        assertEquals(99, dto.getCreatorId());
    }

    @Test
    void testFromDTO() {
        MediaDTO dto = new MediaDTO(100, "Inception", "Dreams", "MOVIE", 2010, List.of("SCIENCE_FICTION"), 12, 99, 9.0);
        when(userRepository.findById(99)).thenReturn(null); // Return null entity, userMapper will handle or mocked
        when(ratingRepository.findByMediaId(100)).thenReturn(Collections.emptyList());

        Media media = mediaMapper.fromDTO(dto);

        assertEquals("Inception", media.getTitle());
        assertEquals(MediaType.MOVIE, media.getMediaType());
        assertEquals(1, media.getGenres().size());
        assertEquals(Genre.SCIENCE_FICTION, media.getGenres().get(0));
    }

    @Test
    void testToEntity() {
        User creator = new User();
        creator.setId(99);
        Media media = new Media(100, "Inception", "Dreams", MediaType.MOVIE, 2010, List.of(Genre.SCIENCE_FICTION), 12, creator, 9.0, Collections.emptyList());

        MediaEntity entity = mediaMapper.toEntity(media);

        assertEquals("Inception", entity.getTitle());
        assertEquals(MediaType.MOVIE.ordinal(), entity.getMediaType());
        assertEquals(99, entity.getCreatorId());
    }

    @Test
    void testFromEntity() {
        MediaEntity entity = new MediaEntity(100, "Inception", "Dreams", MediaType.MOVIE.ordinal(), 2010, List.of(Genre.SCIENCE_FICTION.ordinal()), 12, 99);
        when(userRepository.findById(99)).thenReturn(null);
        when(ratingRepository.findByMediaId(100)).thenReturn(Collections.emptyList());

        Media media = mediaMapper.fromEntity(entity);

        assertEquals("Inception", media.getTitle());
        assertEquals(MediaType.MOVIE, media.getMediaType());
    }

    @Test
    void testGenreMapping() {
        User creator = new User();
        creator.setId(1);
        Media media = new Media(1, "G", "D", MediaType.GAME, 2000, List.of(Genre.HORROR, Genre.COMEDY), 0, creator, 5.0, Collections.emptyList());
        
        MediaDTO dto = mediaMapper.toDTO(media);
        
        assertTrue(dto.getGenres().contains("HORROR"));
        assertTrue(dto.getGenres().contains("COMEDY"));
    }
}
