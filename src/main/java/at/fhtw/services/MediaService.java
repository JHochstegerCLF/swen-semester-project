package at.fhtw.services;

import at.fhtw.converter.JsonConverter;
import at.fhtw.models.Media;
import at.fhtw.models.Rating;
import at.fhtw.persistence.MediaRepository;
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

    public Response addMedia(Media media) {
        List<Media> medias = mediaRepository.getMedias();
        media.setId(medias.size());
        if (mediaRepository.addMedia(media)) {
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
        JsonConverter<Media> jsonConverter = new JsonConverter<>(Media.class);
        Media media = mediaRepository.getMediaById(id);
        if (media != null) {
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    jsonConverter.serialize(media)
            );
        }
        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.PLAIN_TEXT,
                "Media not found"
        );
    }

    public Response updateMedia(int id, Media media) {
        if (mediaRepository.getMediaById(id) == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Media not found"
            );
        }
        if (mediaRepository.updateMedia(id, media)) {
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
        if (mediaRepository.getMediaById(id) == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "Media not found"
            );
        }
        if (mediaRepository.deleteMedia(id)) {
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
        List<Media> medias = mediaRepository.getMedias();
        if (medias == null) {
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.PLAIN_TEXT,
                    "Something went wrong"
            );
        }
        Stream<Media> mediaStream = medias.stream();
        if (filter.containsKey("title")) {
            mediaStream = mediaStream.filter(m -> m.getTitle().toLowerCase().contains(filter.get("title").toLowerCase()));
        }
        if (filter.containsKey("genre")) {
            mediaStream = mediaStream.filter(m -> m.getGenres().stream().anyMatch(g -> g.toLowerCase().contains(filter.get("genre").toLowerCase())));
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
        JsonConverter<List> jsonConverter = new JsonConverter<>(List.class);
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                jsonConverter.serialize(filteredMedias)
        );
    }
}