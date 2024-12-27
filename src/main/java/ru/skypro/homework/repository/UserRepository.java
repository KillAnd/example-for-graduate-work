package ru.skypro.homework.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.User;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * Предоставляет методы для выполнения операций с пользователями в базе данных.
 * Расширяет {@link JpaRepository}, что позволяет использовать стандартные методы JPA.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по его email.
     *
     * @param email Email пользователя.
     * @return {@link Optional}, содержащий пользователя, если он найден, иначе пустой {@link Optional}.
     */
    Optional<User> findByEmail(String email);

    /**
     * Находит пользователя по его уникальному идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return {@link Optional}, содержащий пользователя, если он найден, иначе пустой {@link Optional}.
     */
    Optional<User> findById(Long id);

    /**
     * Находит пользователя по его номеру телефона.
     *
     * @param phone Номер телефона пользователя.
     * @return Найденный пользователь или {@code null}, если пользователь не найден.
     */
    User findByPhone(String phone);
}