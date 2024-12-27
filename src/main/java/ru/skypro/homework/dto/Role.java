package ru.skypro.homework.dto;

/**
 * Перечисление, представляющее роли пользователей в системе.
 * Возможные значения:
 * <ul>
 *     <li>{@link #USER} - Обычный пользователь.</li>
 *     <li>{@link #ADMIN} - Администратор.</li>
 * </ul>
 */
public enum Role {

    /**
     * Роль обычного пользователя.
     */
    USER,

    /**
     * Роль администратора.
     */
    ADMIN
}