package at.fhtw.persistence;


import at.fhtw.models.Media;
import at.fhtw.services.MediaService;
import com.google.inject.Singleton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class MediaRepository {
    private final List<Media> medias = new ArrayList();

    private List<Media> getMedia() {
        return medias;
    }

    public boolean addMedia(Media media) {
        return medias.add(media);
    }

    public Media getMediaById(int id) {
        return medias.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
    }

    public boolean updateMedia(int id, Media media) {
        Optional<Media> possibleMedia = medias.stream().filter(m -> m.getId() == id).findFirst();
        possibleMedia.ifPresent(value -> value.update(media));
        return possibleMedia.isPresent();

    }

    public boolean deleteMedia(int id) {
        Optional<Media> possibleMedia = medias.stream().filter(m -> m.getId() == id).findFirst();
        possibleMedia.ifPresent(medias::remove);
        return possibleMedia.isPresent();
    }

    public List<Media> getMedias() {
        return medias;
    }
}
