package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Сущность, представляющая пользователя (User).
 * Содержит информацию о пользователе, включая его идентификатор, логин, пароль, имя, фамилию, роль, телефон,
 * изображение, а также связи с объявлениями и комментариями.
 */
@Data
@Entity
@Table(name = "app_user")
@ToString(exclude = {"adsListForUsers", "commentsListForUsers", "imageUsers"})
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Логин пользователя.
     */
    private String username;

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
     * Роль пользователя.
     */
    private Role role;

    /**
     * Телефон пользователя.
     */
    private String phone;

    /**
     * Ссылка на изображение (аватар) пользователя.
     */
    private String image;

    /**
     * Список объявлений, созданных пользователем.
     * Связь с сущностью Ad.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "userAd", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ad> adsListForUsers;

    /**
     * Список комментариев, оставленных пользователем.
     * Связь с сущностью Comment.
     */
    @OneToMany(mappedBy = "userCom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> commentsListForUsers;

    /**
     * Изображение, связанное с пользователем.
     * Связь с сущностью Image.
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_image_id", referencedColumnName = "id")
    private Image imageUsers;
}