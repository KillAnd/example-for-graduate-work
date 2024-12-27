package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

/**
 * Класс, реализующий интерфейс {@link UpdateUserMapper}.
 * Предоставляет методы для преобразования объектов между DTO и сущностями,
 * связанными с обновлением данных пользователя.
 */
@Component
public class UserMapperImpl implements UpdateUserMapper {

    /**
     * Преобразует сущность {@link User} в объект {@link UpdateUser}.
     *
     * @param user Сущность {@link User}, представляющая пользователя.
     * @return Объект {@link UpdateUser}, содержащий данные для обновления пользователя.
     */
    @Override
    public UpdateUser mapToUpdateUser(User user) {
        UpdateUser dto = new UpdateUser();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        return dto;
    }

    /**
     * Преобразует объект {@link UpdateUser} в сущность {@link User}.
     *
     * @param dto Объект {@link UpdateUser}, содержащий данные для обновления пользователя.
     * @return Сущность {@link User} с обновлёнными данными.
     */
    @Override
    public User mapFromUpdateUser(UpdateUser dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        return user;
    }
}
