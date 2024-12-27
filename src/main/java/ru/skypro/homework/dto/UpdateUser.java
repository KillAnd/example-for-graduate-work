package ru.skypro.homework.dto;

import lombok.Getter;
import ru.skypro.homework.exception.UpdateUserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс, представляющий данные для обновления информации о пользователе.
 * Содержит ограничения на длину имени, фамилии и формат номера телефона,
 * а также выполняет валидацию при установке значений.
 */
public class UpdateUser {

    /**
     * Минимальная длина имени пользователя.
     */
    private final int FIRSTNAME_MIN = 3;

    /**
     * Максимальная длина имени пользователя.
     */
    private final int FIRSTNAME_MAX = 10;

    /**
     * Минимальная длина фамилии пользователя.
     */
    private final int LASTNAME_MIN = 3;

    /**
     * Максимальная длина фамилии пользователя.
     */
    private final int LASTNAME_MAX = 10;

    /**
     * Регулярное выражение для проверки формата номера телефона.
     */
    private final String PHONE_REGEX = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}";

    /**
     * Имя пользователя.
     * -- GETTER --
     *  Возвращает имя пользователя.
     *
     * @return Имя пользователя.

     */
    @Getter
    private String firstName;

    /**
     * Фамилия пользователя.
     * -- GETTER --
     *  Возвращает фамилию пользователя.
     *
     * @return Фамилия пользователя.

     */
    @Getter
    private String lastName;

    /**
     * Номер телефона пользователя.
     * -- GETTER --
     *  Возвращает номер телефона пользователя.
     *
     * @return Номер телефона пользователя.

     */
    @Getter
    private String phone;

    /**
     * Конструктор по умолчанию.
     */
    public UpdateUser() {
    }

    /**
     * Устанавливает имя пользователя. Выполняет валидацию длины имени.
     *
     * @param firstName Имя пользователя.
     * @throws UpdateUserException Если длина имени не соответствует ограничениям.
     */
    public void setFirstName(String firstName) throws UpdateUserException {
        if (firstName.length() >= FIRSTNAME_MIN && firstName.length() <= FIRSTNAME_MAX) {
            this.firstName = firstName;
        } else {
            throw new UpdateUserException("first name length");
        }
    }

    /**
     * Устанавливает фамилию пользователя. Выполняет валидацию длины фамилии.
     *
     * @param lastName Фамилия пользователя.
     * @throws UpdateUserException Если длина фамилии не соответствует ограничениям.
     */
    public void setLastName(String lastName) throws UpdateUserException {
        if (lastName.length() >= LASTNAME_MIN && lastName.length() <= LASTNAME_MAX) {
            this.lastName = lastName;
        } else {
            throw new UpdateUserException("last name length");
        }
    }

    /**
     * Устанавливает номер телефона пользователя. Выполняет валидацию формата номера.
     *
     * @param phone Номер телефона пользователя.
     * @throws UpdateUserException Если формат номера телефона не соответствует регулярному выражению.
     */
    public void setPhone(String phone) throws UpdateUserException {
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            this.phone = phone;
        } else {
            throw new UpdateUserException("phone pattern");
        }
    }
}
