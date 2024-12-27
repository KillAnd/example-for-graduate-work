package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Класс, представляющий данные для регистрации нового пользователя.
 * Содержит информацию, необходимую для создания учётной записи,
 * такую как email, пароль, имя, фамилия, телефон и роль.
 */
@Data
public class Register {

    /**
     * Email пользователя, использутся как логин
     */
    private String email;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Имя пользователя.
     */
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    private String lastName;

    /**
     * Номер телефона пользователя.
     */
    private String phone;

    /**
     * Роль пользователя в системе.
     */
    private Role role;
}
