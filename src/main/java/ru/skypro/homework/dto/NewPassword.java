package ru.skypro.homework.dto;

import lombok.Getter;
import ru.skypro.homework.exception.NewPasswordException;

/**
 * Класс, представляющий данные для изменения пароля пользователя.
 * Содержит ограничения на длину пароля и выполняет валидацию
 * при установке значений текущего и нового пароля.
 */
public class NewPassword {

    /**
     * Минимальная длина пароля.
     */
    private final int MIN = 8;

    /**
     * Максимальная длина пароля.
     */
    private final int MAX = 16;

    /**
     * Текущий пароль пользователя.
     * -- GETTER --
     *  Возвращает текущий пароль пользователя.
     *
     * @return Текущий пароль пользователя.

     */
    @Getter
    private String currentPassword;

    /**
     * Новый пароль пользователя.
     * -- GETTER --
     *  Возвращает новый пароль пользователя.
     *
     * @return Новый пароль пользователя.

     */
    @Getter
    private String newPassword;

    /**
     * Конструктор по умолчанию.
     */
    public NewPassword() {
    }

    /**
     * Устанавливает текущий пароль пользователя. Выполняет валидацию длины пароля.
     *
     * @param currentPassword Текущий пароль пользователя.
     * @throws NewPasswordException Если длина пароля не соответствует ограничениям.
     */
    public void setCurrentPassword(String currentPassword) throws NewPasswordException {
        if (currentPassword.length() >= MIN && currentPassword.length() <= MAX) {
            this.currentPassword = currentPassword;
        } else {
            throw new NewPasswordException("current password length");
        }
    }

    /**
     * Устанавливает новый пароль пользователя. Выполняет валидацию длины пароля.
     *
     * @param newPassword Новый пароль пользователя.
     * @throws NewPasswordException Если длина пароля не соответствует ограничениям.
     */
    public void setNewPassword(String newPassword) throws NewPasswordException {
        if (newPassword.length() >= MIN && newPassword.length() <= MAX) {
            this.newPassword = newPassword;
        } else {
            throw new NewPasswordException("new password length");
        }
    }
}
