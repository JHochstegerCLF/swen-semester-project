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


    @Inject
    public FavoriteMapper(
            UserRepository userRepository,
            MediaRepository mediaRepository
    ) {
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
    }

    public FavoriteDTO toDTO(Favorite favorite) {
        return new FavoriteDTO(
                favorite.getId(),
                favorite.getUser().getId(),
                favorite.getMedia().getId()
        );
    }

    public Favorite fromDTO(FavoriteDTO favoriteDTO) {
        at.fhtw.models.entities.UserEntity userEntity = userRepository.findById(favoriteDTO.getUser());
        at.fhtw.models.User user = null;
        if (userEntity != null) {
            user = new at.fhtw.models.User(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getEmail(), userEntity.getFavoriteGenre() != null ? at.fhtw.models.enums.Genre.values()[userEntity.getFavoriteGenre()] : null, new java.util.ArrayList<>(), new java.util.ArrayList<>());
        }

        at.fhtw.models.entities.MediaEntity mediaEntity = mediaRepository.findById(favoriteDTO.getMedia());
        at.fhtw.models.Media media = null;
        if (mediaEntity != null) {
            media = new at.fhtw.models.Media(mediaEntity.getId(), mediaEntity.getTitle(), mediaEntity.getDescription(), mediaEntity.getMediaType() != null ? at.fhtw.models.enums.MediaType.values()[mediaEntity.getMediaType()] : null, mediaEntity.getReleaseYear(), mediaEntity.getGenres() != null ? mediaEntity.getGenres().stream().map(g -> at.fhtw.models.enums.Genre.values()[g]).toList() : null, mediaEntity.getAgeRestriction(), null, 0.0, new java.util.ArrayList<>());
        }

        return new Favorite(
                favoriteDTO.getId(),
                user,
                media
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
        at.fhtw.models.entities.UserEntity userEnt = userRepository.findById(favoriteEntity.getUser());
        at.fhtw.models.User user = null;
        if (userEnt != null) {
            user = new at.fhtw.models.User(userEnt.getId(), userEnt.getUsername(), userEnt.getPassword(), userEnt.getEmail(), userEnt.getFavoriteGenre() != null ? at.fhtw.models.enums.Genre.values()[userEnt.getFavoriteGenre()] : null, new java.util.ArrayList<>(), new java.util.ArrayList<>());
        }

        at.fhtw.models.entities.MediaEntity medEnt = mediaRepository.findById(favoriteEntity.getMedia());
        at.fhtw.models.Media media = null;
        if (medEnt != null) {
            media = new at.fhtw.models.Media(medEnt.getId(), medEnt.getTitle(), medEnt.getDescription(), medEnt.getMediaType() != null ? at.fhtw.models.enums.MediaType.values()[medEnt.getMediaType()] : null, medEnt.getReleaseYear(), medEnt.getGenres() != null ? medEnt.getGenres().stream().map(g -> at.fhtw.models.enums.Genre.values()[g]).toList() : null, medEnt.getAgeRestriction(), null, 0.0, new java.util.ArrayList<>());
        }

        return new Favorite(
                favoriteEntity.getId(),
                user,
                media
        );
    }
}
