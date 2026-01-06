package at.fhtw.mapper;

import at.fhtw.models.Media;
import at.fhtw.models.dtos.MediaDTO;
import at.fhtw.models.entities.MediaEntity;

public interface IMediaMapper {
    MediaDTO toDTO(Media media);

    Media fromDTO(MediaDTO mediaDTO);

    MediaEntity toEntity(Media media);

    Media fromEntity(MediaEntity mediaEntity);
}
