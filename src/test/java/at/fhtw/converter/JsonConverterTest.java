package at.fhtw.converter;

import at.fhtw.models.dtos.MediaDTO;
import at.fhtw.models.dtos.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonConverterTest {

    private final JsonConverter<UserDTO> userConverter = new JsonConverter<>(UserDTO.class);
    private final JsonConverter<MediaDTO> mediaConverter = new JsonConverter<>(MediaDTO.class);

    @Test
    void testSerializeUserDTO() {
        UserDTO user = new UserDTO(1, "jonas", "password", "test@test.com", "ACTION", Collections.emptyList());
        String json = userConverter.serialize(user);
        assertNotNull(json);
        assertTrue(json.contains("\"username\" : \"jonas\""));
    }

    @Test
    void testDeserializeUserDTO() {
        String json = "{ \"id\": 1, \"username\": \"jonas\", \"password\": \"password\", \"email\": \"test@test.com\", \"favoriteGenre\": \"ACTION\", \"favorites\": [] }";
        UserDTO user = userConverter.deserialize(json);
        assertEquals("jonas", user.getUsername());
        assertEquals(1, user.getId());
    }

    @Test
    void testSerializeMediaDTO() {
        MediaDTO media = new MediaDTO(100, "Movie", "Desc", "MOVIE", 2020, List.of("ACTION"), 12, 1, 8.5);
        String json = mediaConverter.serialize(media);
        assertNotNull(json);
        assertTrue(json.contains("\"title\" : \"Movie\""));
    }

    @Test
    void testDeserializeMediaDTO() {
        // Note: creatorId maps to "creator" via @JsonProperty
        String json = "{ \"id\": 100, \"title\": \"Movie\", \"mediaType\": \"MOVIE\", \"creator\": 1 }";
        MediaDTO media = mediaConverter.deserialize(json);
        assertEquals("Movie", media.getTitle());
        assertEquals(1, media.getCreatorId());
    }

    @Test
    void testSerializeNull() {
        String json = userConverter.serialize(null);
        assertEquals("null", json);
    }
}
