package ru.skypro.homework.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса UserDetails для предоставления информации о пользователе.
 * Этот класс используется Spring Security для аутентификации и авторизации пользователей.
 */
public class MyUserDetails implements UserDetails {

    private final User user;

    /**
     * Конструктор для создания объекта MyUserDetails на основе сущности User.
     *
     * @param user сущность пользователя
     */
    public MyUserDetails(User user) {
        this.user = user;
    }

    /**
     * Возвращает список ролей пользователя.
     *
     * @return коллекция объектов GrantedAuthority, представляющих роли пользователя
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return зашифрованный пароль пользователя
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Возвращает имя пользователя (уникальный идентификатор).
     *
     * @return имя пользователя
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Указывает, не истек ли срок действия аккаунта пользователя.
     *
     * @return true, если срок действия аккаунта не истек, иначе false
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Указывает, не заблокирован ли аккаунт пользователя.
     *
     * @return true, если аккаунт не заблокирован, иначе false
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Указывает, не истек ли срок действия учетных данных пользователя.
     *
     * @return true, если срок действия учетных данных не истек, иначе false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Указывает, активен ли аккаунт пользователя.
     *
     * @return true, если аккаунт активен, иначе false
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}