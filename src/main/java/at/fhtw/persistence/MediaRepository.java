package at.fhtw.persistence;


import at.fhtw.models.Media;
import at.fhtw.models.entities.MediaEntity;
import at.fhtw.orm.Orm;
import at.fhtw.services.MediaService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class MediaRepository {

    private final Orm<MediaEntity> mediaORM;

    @Inject
    public MediaRepository(Orm<MediaEntity> mediaORM) {
        this.mediaORM = mediaORM;
    }

    public List<MediaEntity> findAll() {
        return mediaORM.getAll();
    }

    public MediaEntity findById(int id) {
        return mediaORM.getById(id);
    }

    public int create(MediaEntity media) {
        return mediaORM.persistEntity(media);
    }

    public MediaEntity update(int id, MediaEntity media) {
        return mediaORM.update(id, media);
    }

    public boolean delete(int id) {
        return mediaORM.delete(id);
    }
}
