package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Сущность, представляющая объявление (Ad).
 * Содержит информацию об объявлении, включая автора, цену, заголовок, изображение, описание,
 * а также связи с пользователем, комментариями и изображением объявления.
 */
@Data
@Entity
@ToString(exclude = {"author", "userAd", "comments", "imageAd"})
public class Ad {

    /**
     * Уникальный идентификатор объявления.
     */
    @Id
    @GeneratedValue
    private int pk;

    /**
     * Идентификатор автора объявления.
     */
    private int author;

    /**
     * Цена объявления.
     */
    private int price;

    /**
     * Заголовок объявления.
     */
    private String title;

    /**
     * Ссылка на изображение объявления.
     */
    private String image;

    /**
     * Описание объявления.
     */
    private String description;

    /**
     * Пользователь, создавший объявление.
     * Связь с сущностью User.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userAd;

    /**
     * Список комментариев к объявлению.
     * Связь с сущностью Comment.
     */
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    /**
     * Изображение, связанное с объявлением.
     * Связь с сущностью Image.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "ad_image_id", referencedColumnName = "id")
    private Image imageAd;
}