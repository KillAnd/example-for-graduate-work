package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.dto.Role;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурационный класс для настройки Spring Security.
 * Определяет правила безопасности, такие как аутентификация, авторизация и CORS,
 * а также настраивает кодировщик паролей и пользователей в памяти.
 */
@Configuration
public class WebSecurityConfig {

    /**
     * Список URL-адресов, которые доступны без аутентификации.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    /**
     * Создает менеджер пользователей в памяти с одним предопределенным пользователем.
     *
     * @param passwordEncoder Кодировщик паролей для шифрования пароля пользователя.
     * @return {@link InMemoryUserDetailsManager} с предопределенным пользователем.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user =
                User.builder()
                        .username("user@gmail.com")
                        .password("password")
                        .passwordEncoder(passwordEncoder::encode)
                        .roles(Role.USER.name())
                        .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Настраивает цепочку фильтров безопасности.
     * Отключает CSRF, разрешает доступ к URL-адресам из белого списка без аутентификации,
     * требует аутентификации для доступа к "/ads/**" и "/users/**", а также включает CORS и HTTP Basic аутентификацию.
     *
     * @param http Объект {@link HttpSecurity} для настройки безопасности.
     * @return Сконфигурированная цепочка фильтров безопасности.
     * @throws Exception Если возникает ошибка при настройке.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Создает и возвращает кодировщик паролей BCrypt.
     *
     * @return {@link BCryptPasswordEncoder} для шифрования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
