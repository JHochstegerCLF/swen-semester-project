package at.fhtw.mapper;

import at.fhtw.models.User;
import at.fhtw.models.dtos.UserDTO;
import at.fhtw.models.entities.UserEntity;
import at.fhtw.models.enums.Genre;
import jakarta.inject.Inject;

public class UserMapper {

    @Inject
    public UserMapper() {
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFavoriteGenre().toString()
        );
    }

    public User fromDTO(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                Genre.valueOf(userDTO.getFavoriteGenre())
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFavoriteGenre().ordinal()
        );
    }

    public User fromEntity(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                Genre.values()[userEntity.getFavoriteGenre()]
        );
    }
}
