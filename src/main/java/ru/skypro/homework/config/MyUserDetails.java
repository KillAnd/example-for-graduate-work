package ru.skypro.homework.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Возвращаем роли пользователя
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        // Возвращаем зашифрованный пароль пользователя
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // Возвращаем уникальный идентификатор пользователя
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Указываем, что аккаунт не просрочен
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Указываем, что аккаунт не заблокирован
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Указываем, что учетные данные не просрочены
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Указываем, что аккаунт активен
        return true;
    }
}
