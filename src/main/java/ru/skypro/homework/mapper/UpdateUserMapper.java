package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

@Mapper
public interface UpdateUserMapper {

    UpdateUser mapToUpdateUser(User user);
    User mapFromUpdateUser(UpdateUser dto);
}
