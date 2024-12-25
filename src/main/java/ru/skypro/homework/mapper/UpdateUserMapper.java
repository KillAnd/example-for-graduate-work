package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

public interface UpdateUserMapper {

    UpdateUser mapToUpdateUser(User user);
    User mapFromUpdateUser(UpdateUser dto);
}
