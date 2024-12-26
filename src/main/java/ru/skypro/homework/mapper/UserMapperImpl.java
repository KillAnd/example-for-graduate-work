package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;

@Component
public class UserMapperImpl implements UpdateUserMapper {

    @Override
    public UpdateUser mapToUpdateUser(User user) {
        UpdateUser dto = new UpdateUser();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        return dto;
    }

    @Override
    public User mapFromUpdateUser(UpdateUser dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        return user;
    }

    @Override
    public User toUserRegister(Register register) {
        if (register == null) {
            throw new UserNotFoundException("Переданный объект register is null");
        }
        User userRegister = new User();
        userRegister.setUsername(register.getUsername());
        userRegister.setPassword(register.getPassword());
        userRegister.setFirstName(register.getFirstName());
        userRegister.setLastName(register.getLastName());
        userRegister.setPhone(register.getPhone());
        userRegister.setRole(register.getRole());
        return userRegister;
    }
}
