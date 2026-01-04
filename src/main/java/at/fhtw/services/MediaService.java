package at.fhtw.services;

import at.fhtw.converter.JsonConverter;
import at.fhtw.mapper.MediaMapper;
import at.fhtw.models.Media;
import at.fhtw.models.Rating;
import at.fhtw.models.dtos.MediaDTO;
import at.fhtw.models.entities.MediaEntity;
import at.fhtw.persistence.MediaRepository;
import at.fhtw.persistence.RatingRepository;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Response;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@AllArgsConstructor(onConstructor_ = @Inject)
public class MediaService {
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;

    public Response addMedia(MediaDTO media) {
        if (mediaRepository.create(mediaMapper.toEntity(mediaMapper.fromDTO(media))) != -1) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Media created"
            );
        }
        ;
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response getMedia(int id) {
        JsonConverter<MediaDTO> jsonConverter = new JsonConverter<>(MediaDTO.class);
        Media media = mediaMapper.fromEntity(mediaRepository.findById(id));
        if (media != null) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    jsonConverter.serialize(mediaMapper.toDTO(media))
            );
        }
        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.PLAIN_TEXT,
                "Media not found"
        );
    }

    public Response updateMedia(int id, MediaDTO media) {
        if (mediaRepository.findById(id) == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Media not found"
            );
        }
        if (mediaRepository.update(id, mediaMapper.toEntity(mediaMapper.fromDTO(media))) != null) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Media updated"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response deleteMedia(int id) {
        if (mediaRepository.findById(id) == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Media not found"
            );
        }
        if (mediaRepository.delete(id)) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Media deleted"
            );
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.PLAIN_TEXT,
                "Something went wrong"
        );
    }

    public Response getAllMedia(Map<String, String> filter) {
        Stream<Media> mediaStream = mediaRepository.findAll().stream().map(mediaMapper::fromEntity);
        if (filter.containsKey("title")) {
            mediaStream = mediaStream.filter(m -> m.getTitle().toLowerCase().contains(filter.get("title").toLowerCase()));
        }
        if (filter.containsKey("genre")) {
            mediaStream = mediaStream.filter(m -> m.getGenres().stream().anyMatch(g -> Integer.parseInt(filter.get("genre")) == g.ordinal()));
        }
        if (filter.containsKey("mediaType")) {
            mediaStream = mediaStream.filter(m -> m.getMediaType().toString().toLowerCase().contains(filter.get("mediaType").toLowerCase()));
        }
        if (filter.containsKey("releaseYear")) {
            mediaStream = mediaStream.filter(m -> m.getReleaseYear() == Integer.parseInt(filter.get("releaseYear").toLowerCase()));
        }
        if (filter.containsKey("ageRestriction")) {
            mediaStream = mediaStream.filter(m -> m.getAgeRestriction() == Integer.parseInt(filter.get("ageRestriction").toLowerCase()));
        }
        if (filter.containsKey("rating")) {
            mediaStream = mediaStream.filter(m -> m.getRating() == Integer.parseInt(filter.get("rating").toLowerCase()));
        }

        List<Media> filteredMedias = new java.util.ArrayList<>(mediaStream.toList());
        if (filter.containsKey("sortBy")) {
            switch (filter.get("sortBy")) {
                case "title":
                    filteredMedias.sort(Comparator.comparing(Media::getTitle));
                    break;
                case "year":
                    filteredMedias.sort(Comparator.comparingInt(Media::getReleaseYear));
                    break;
                case "score":
                    filteredMedias.sort(Comparator.comparingInt(Media::getRating));
                    break;
            }
        }
        List<MediaDTO> mediaDTOS= filteredMedias.stream().map(mediaMapper::toDTO).toList();
        JsonConverter<List> jsonConverter = new JsonConverter<>(List.class);
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                jsonConverter.serialize(mediaDTOS)
        );
    }
}