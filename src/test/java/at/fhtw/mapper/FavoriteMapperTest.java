package at.fhtw.mapper;

import at.fhtw.models.Favorite;
import at.fhtw.models.Media;
import at.fhtw.models.User;
import at.fhtw.models.dtos.FavoriteDTO;
import at.fhtw.models.entities.FavoriteEntity;
import at.fhtw.persistence.MediaRepository;
import at.fhtw.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteMapperTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private MediaRepository mediaRepository;
    @Mock
    private IUserMapper userMapper;
    @Mock
    private IMediaMapper mediaMapper;

    private FavoriteMapper favoriteMapper;

    @BeforeEach
    void setUp() {
        favoriteMapper = new FavoriteMapper(userRepository, mediaRepository, userMapper, mediaMapper);
    }

    @Test
    void testToDTO() {
        User user = new User();
        user.setId(1);
        Media media = new Media();
        media.setId(2);
        Favorite favorite = new Favorite(10, user, media);

        FavoriteDTO dto = favoriteMapper.toDTO(favorite);

        assertEquals(10, dto.getId());
        assertEquals(1, dto.getUser());
        assertEquals(2, dto.getMedia());
    }

    @Test
    void testToEntity() {
        User user = new User();
        user.setId(1);
        Media media = new Media();
        media.setId(2);
        Favorite favorite = new Favorite(10, user, media);

        FavoriteEntity entity = favoriteMapper.toEntity(favorite);

        assertEquals(10, entity.getId());
        assertEquals(1, entity.getUser());
        assertEquals(2, entity.getMedia());
    }

    @Test
    void testFromDTO() {
        FavoriteDTO dto = new FavoriteDTO(10, 1, 2);
        User user = new User();
        user.setId(1);
        Media media = new Media();
        media.setId(2);

        when(userRepository.findById(1)).thenReturn(null);
        when(mediaRepository.findById(2)).thenReturn(null);
        when(userMapper.fromEntity(null)).thenReturn(user);
        when(mediaMapper.fromEntity(null)).thenReturn(media);

        Favorite favorite = favoriteMapper.fromDTO(dto);

        assertEquals(10, favorite.getId());
        assertEquals(1, favorite.getUser().getId());
        assertEquals(2, favorite.getMedia().getId());
    }

    @Test
    void testFromEntity() {
        FavoriteEntity entity = new FavoriteEntity(10, 1, 2);
        User user = new User();
        user.setId(1);
        Media media = new Media();
        media.setId(2);

        when(userRepository.findById(1)).thenReturn(null);
        when(mediaRepository.findById(2)).thenReturn(null);
        when(userMapper.fromEntity(null)).thenReturn(user);
        when(mediaMapper.fromEntity(null)).thenReturn(media);

        Favorite favorite = favoriteMapper.fromEntity(entity);

        assertEquals(10, favorite.getId());
        assertEquals(1, favorite.getUser().getId());
    }
}
