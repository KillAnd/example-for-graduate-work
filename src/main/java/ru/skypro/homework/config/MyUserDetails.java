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
 * Класс, реализующий интерфейс {@link UserDetails} для предоставления информации о пользователе.
 * Используется Spring Security для аутентификации и авторизации.
 * Содержит данные о пользователе, такие как пароль, email, роль и статус аккаунта.
 */
public class MyUserDetails implements UserDetails {

    private final User user;

    /**
     * Конструктор для создания экземпляра MyUserDetails.
     *
     * @param user Объект {@link User}, содержащий данные пользователя.
     */
    public MyUserDetails(User user) {
        this.user = user;
    }

    /**
     * Возвращает список ролей пользователя.
     *
     * @return Коллекция объектов {@link GrantedAuthority}, представляющих роли пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return Зашифрованный пароль пользователя.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Возвращает уникальный идентификатор пользователя (email).
     *
     * @return Email пользователя.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Указывает, что аккаунт пользователя не просрочен.
     *
     * @return Всегда {@code true}, так как аккаунт не имеет срока действия.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Указывает, что аккаунт пользователя не заблокирован.
     *
     * @return Всегда {@code true}, так как аккаунт не блокируется.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Указывает, что учетные данные пользователя не просрочены.
     *
     * @return Всегда {@code true}, так как учетные данные не имеют срока действия.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Указывает, что аккаунт пользователя активен.
     *
     * @return Всегда {@code true}, так как аккаунт всегда активен.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
