package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;

@Component
public class UserMapperImpl {

    public User toUserEntity(Register register) {
        if (register == null) {
            throw new NullPointerException("Переданный объект register is null");
        }
        User userEntity = new User();
        userEntity.setUsername(register.getUsername());
        userEntity.setPassword(register.getPassword());
        userEntity.setFirstName(register.getFirstName());
        userEntity.setLastName(register.getLastName());
        userEntity.setPhone(register.getPhone());
        userEntity.setRole(register.getRole());
        return userEntity;
    }

    // Маппинг из UserEntity в User DTO
    public UserDTO toUserDto(User userEntity) {
        if (userEntity == null) {
            throw new NullPointerException("Переданный объект userEntity is null");
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(userEntity.getId());
        userDto.setEmail(userEntity.getUsername());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setPhone(userEntity.getPhone());
        userDto.setRole(userEntity.getRole());
        userDto.setImage(userEntity.getImage());

        return userDto;
    }

    public UpdateUser mapToUpdateUser(User userEntity) {
        if (userEntity == null) {
            throw new NullPointerException("Переданный объект userEntity is null");
        }
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName(userEntity.getFirstName());
        updateUser.setLastName(userEntity.getLastName());
        updateUser.setPhone(userEntity.getPhone());
        return updateUser;
    }

    public User mapFromUpdateUser(UpdateUser userUpdate) {
        if (userUpdate == null) {
            throw new NullPointerException("Переданный объект userEntity is null");
        }
        User userEntity = new User();
        userEntity.setFirstName(userUpdate.getFirstName());
        userEntity.setLastName(userUpdate.getLastName());
        userEntity.setPhone(userUpdate.getPhone());
        return userEntity;
    }
}
