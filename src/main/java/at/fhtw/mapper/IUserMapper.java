package at.fhtw.mapper;

import at.fhtw.models.User;
import at.fhtw.models.dtos.UserDTO;
import at.fhtw.models.entities.UserEntity;

public interface IUserMapper {
    UserDTO toDTO(User user);

    User fromDTO(UserDTO userDTO);

    UserEntity toEntity(User user);

    User fromEntity(UserEntity userEntity);
}