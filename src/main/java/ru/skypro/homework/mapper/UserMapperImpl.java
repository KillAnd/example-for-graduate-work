package ru.skypro.homework.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;

/**
 * Реализация маппера для преобразования между сущностью User и её DTO представлениями.
 * Этот класс предоставляет методы для преобразования объектов Register, UserDTO и UpdateUser
 * в сущность User и наоборот.
 */
@Component
public class UserMapperImpl {

    @Value("${base.url.for.images}")
    private String baseURL;

    /**
     * Преобразует объект Register в сущность User.
     *
     * @param register объект Register, содержащий данные для регистрации
     * @return сущность User
     * @throws NullPointerException если переданный объект register равен null
     */
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

    /**
     * Преобразует сущность User в объект UserDTO.
     *
     * @param userEntity сущность User
     * @return объект UserDTO
     * @throws NullPointerException если переданный объект userEntity равен null
     */
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
        userDto.setImage(userEntity.getImage().replace('\\', '/'));

        return userDto;
    }

    /**
     * Преобразует сущность User в объект UpdateUser.
     *
     * @param userEntity сущность User
     * @return объект UpdateUser
     * @throws NullPointerException если переданный объект userEntity равен null
     */
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

    /**
     * Преобразует объект UpdateUser в сущность User.
     *
     * @param userUpdate объект UpdateUser
     * @return сущность User
     * @throws NullPointerException если переданный объект userUpdate равен null
     */
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