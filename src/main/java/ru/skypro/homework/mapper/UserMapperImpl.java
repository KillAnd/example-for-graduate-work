package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

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
}
