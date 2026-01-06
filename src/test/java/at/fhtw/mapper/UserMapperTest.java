package at.fhtw.mapper;

import at.fhtw.models.Media;
import at.fhtw.models.User;
import at.fhtw.models.dtos.MediaDTO;
import at.fhtw.models.dtos.UserDTO;
import at.fhtw.models.entities.UserEntity;
import at.fhtw.models.enums.Genre;
import at.fhtw.persistence.MediaRepository;
import at.fhtw.persistence.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private IMediaMapper mediaMapper;
    @Mock
    private MediaRepository mediaRepository;
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private IRatingMapper ratingMapper;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper(mediaMapper, mediaRepository, ratingRepository, ratingMapper);
    }

    @Test
    void testToDTO() {
        User user = new User(1, "testuser", "pass", "test@mail.com", Genre.ACTION_AND_ADVENTURE, Collections.emptyList(), Collections.emptyList());

        UserDTO dto = userMapper.toDTO(user);

        assertEquals(1, dto.getId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("ACTION_AND_ADVENTURE", dto.getFavoriteGenre());
    }

    @Test
    void testFromDTO() {
        UserDTO dto = new UserDTO(1, "testuser", "pass", "test@mail.com", "ACTION_AND_ADVENTURE", Collections.emptyList());
        when(ratingRepository.findByUserId(1)).thenReturn(Collections.emptyList());

        User user = userMapper.fromDTO(dto);

        assertEquals(1, user.getId());
        assertEquals(Genre.ACTION_AND_ADVENTURE, user.getFavoriteGenre());
    }

    @Test
    void testToEntity() {
        User user = new User(1, "testuser", "pass", "test@mail.com", Genre.ACTION_AND_ADVENTURE, Collections.emptyList(), Collections.emptyList());

        UserEntity entity = userMapper.toEntity(user);

        assertEquals(1, entity.getId());
        assertEquals(Genre.ACTION_AND_ADVENTURE.ordinal(), entity.getFavoriteGenre());
    }

    @Test
    void testFromEntity() {
        UserEntity entity = new UserEntity(1, "testuser", "pass", "test@mail.com", Genre.ACTION_AND_ADVENTURE.ordinal(), Collections.emptyList());
        when(ratingRepository.findByUserId(1)).thenReturn(Collections.emptyList());

        User user = userMapper.fromEntity(entity);

        assertEquals("testuser", user.getUsername());
        assertEquals(Genre.ACTION_AND_ADVENTURE, user.getFavoriteGenre());
    }

    @Test
    void testToDTOWithFavorites() {
        Media media = new Media();
        media.setId(10);
        User user = new User(1, "testuser", "pass", "test@mail.com", Genre.DRAMA, List.of(media), Collections.emptyList());

        when(mediaMapper.toDTO(any(Media.class))).thenReturn(new MediaDTO());

        UserDTO dto = userMapper.toDTO(user);

        assertEquals(1, dto.getFavorites().size());
    }
}
