package at.fhtw.services;

import at.fhtw.converter.JsonConverter;
import at.fhtw.mapper.MediaMapper;
import at.fhtw.mapper.RatingMapper;
import at.fhtw.mapper.UserMapper;
import at.fhtw.models.Media;
import at.fhtw.models.Rating;
import at.fhtw.models.User;
import at.fhtw.models.dtos.MediaDTO;
import at.fhtw.models.dtos.UserDTO;
import at.fhtw.models.entities.UserEntity;
import at.fhtw.models.enums.Genre;
import at.fhtw.models.enums.MediaType;
import at.fhtw.persistence.FavoriteRepository;
import at.fhtw.persistence.MediaRepository;
import at.fhtw.persistence.RatingRepository;
import at.fhtw.persistence.UserRepository;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Response;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor_ = @Inject)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final FavoriteRepository favoriteRepository;
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;


    public Response getUser(int userId) {
        UserEntity userEntity = userRepository.findById(userId);
        if (userEntity == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "User not found"
            );
        }
        JsonConverter<UserDTO> jsonConverter = new JsonConverter<>(UserDTO.class);
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                jsonConverter.serialize(userMapper.toDTO(userMapper.fromEntity(userEntity)))
        );
    }

    public Response updateUser(int userId, UserDTO userDTO) {
        UserEntity userEntity = userRepository.findById(userDTO.getId());
        if (userEntity == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Profile not found"
            );
        }
        if (userId != userEntity.getId()) {
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.PLAIN_TEXT,
                    "This is not your Profile"
            );
        }

        userEntity.setEmail(userDTO.getEmail());
        userEntity.setFavoriteGenre(userMapper.toEntity(userMapper.fromDTO(userDTO)).getFavoriteGenre());
        if (userRepository.update(userEntity.getId(), userEntity) != null) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Profile updated"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response getRecommmendations(int userId, String type) {
        User user = userMapper.fromEntity(userRepository.findById(userId));
        if (user == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "User not found"
            );
        }
        Map<Genre, Integer> genres = Arrays.stream(Genre.values()).collect(Collectors.toMap(g -> g, g -> 0));
        Map<MediaType, Integer> mediaTypes = Arrays.stream(MediaType.values()).collect(Collectors.toMap(m -> m, m -> 0));
        Map<Integer, Integer> ageRestrictions = new HashMap<>();
        for (Rating rating : user.getRatings()) {
            Media media = rating.getMedia();
            if (rating.isConfirmed()) {
                media.getGenres().forEach(genre -> genres.put(genre, genres.get(genre) + 1));
                mediaTypes.put(media.getMediaType(), mediaTypes.get(media.getMediaType()) + 1);
                ageRestrictions.put(media.getAgeRestriction(), ageRestrictions.getOrDefault(media.getAgeRestriction(), 0) + 1);
            }
        }
        Genre favGenre = Collections.max(genres.entrySet(), Map.Entry.comparingByValue()).getKey();
        MediaType favMediaType = Collections.max(mediaTypes.entrySet(), Map.Entry.comparingByValue()).getKey();
        int favAgeRestriction = Collections.max(ageRestrictions.entrySet(), Map.Entry.comparingByValue()).getKey();

        List<Media> media = mediaRepository.findAll().stream().map(mediaMapper::fromEntity).toList();
        List<Media> favorites = user.getFavorites();
        List<Rating> ratings = user.getRatings().stream().filter(Rating::isConfirmed).toList();
        List<Media> recommendedMedias = media.stream()
                .filter(m -> favorites.stream().noneMatch(f -> f.getId() == m.getId()))
                .filter(m -> ratings.stream().noneMatch(r -> r.getMedia().getId() == m.getId()))
                .filter(m -> m.getGenres().contains(favGenre))
                .toList();
        if (type.equals("content")) {
            recommendedMedias = recommendedMedias.stream()
                    .filter(m -> m.getMediaType() == favMediaType)
                    .filter(m -> m.getAgeRestriction() == favAgeRestriction)
                    .toList();
        }
        Media recommendedMedia = recommendedMedias.stream().max(Comparator.comparingDouble(Media::getRating)).orElse(null);
        if (recommendedMedia == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "No recommendation found"
            );
        }
        JsonConverter<MediaDTO> jsonConverter = new JsonConverter<>(MediaDTO.class);
        return new Response(
                HttpStatus.OK,
                ContentType.PLAIN_TEXT,
                jsonConverter.serialize(mediaMapper.toDTO(recommendedMedia))
        );
    }

    public Response getLeaderBoard() {
        List<User> users = userRepository.findAll().stream().map(userMapper::fromEntity)
                .sorted(Comparator.comparingInt((User u) -> u.getRatings().size()).reversed()).toList();
        JsonConverter<List> jsonConverter = new JsonConverter<>(List.class);
        return new Response(
                HttpStatus.OK,
                ContentType.PLAIN_TEXT,
                jsonConverter.serialize(users.stream().map(userMapper::toDTO).toList())
        );
    }


}
