package at.fhtw.services;

import at.fhtw.converter.JsonConverter;
import at.fhtw.mapper.FavoriteMapper;
import at.fhtw.models.dtos.FavoriteDTO;
import at.fhtw.models.entities.FavoriteEntity;
import at.fhtw.persistence.FavoriteRepository;
import at.fhtw.persistence.MediaRepository;
import at.fhtw.persistence.UserRepository;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Response;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(onConstructor_ = @Inject)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;


    public Response addFavorite(FavoriteDTO favoriteDTO) {
        if (mediaRepository.findById(favoriteDTO.getMedia()) == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Media not found"
            );
        }
        if (favoriteRepository.create(favoriteMapper.toEntity(favoriteMapper.fromDTO(favoriteDTO))) != -1) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Marked as Favorite"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response deleteFavorite(FavoriteDTO favoriteDTO) {
        FavoriteEntity favoriteEntity = favoriteRepository.findByUserId(favoriteDTO.getUser()).stream().filter(f -> f.getMedia() == favoriteDTO.getMedia()).findFirst().orElse(null);
        if (favoriteEntity == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Not marked as favorite"
            );
        }
        if (favoriteRepository.delete(favoriteEntity.getId())) {
            return new Response(
                    HttpStatus.NO_CONTENT,
                    ContentType.PLAIN_TEXT,
                    "Unmarked"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response getFavorites(int userId) {
        if (userRepository.findById(userId) == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "User not found"
            );
        }
        List<FavoriteDTO> favorites = favoriteRepository.findByUserId(userId).stream().map(favoriteMapper::fromEntity).map(favoriteMapper::toDTO).toList();
        JsonConverter<List> jsonConverter = new JsonConverter<>(List.class);
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                jsonConverter.serialize(favorites)
        );
    }
}
