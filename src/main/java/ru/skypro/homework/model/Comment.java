package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * Сущность, представляющая комментарий (Comment).
 * Содержит информацию о комментарии, включая его идентификатор, автора, изображение автора, имя автора,
 * время создания, текст, а также связи с пользователем и объявлением.
 */
@Data
@Entity
public class Comment {

    /**
     * Уникальный идентификатор комментария.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer pk;

    /**
     * Идентификатор автора комментария.
     */
    private Integer author;

    /**
     * Ссылка на изображение автора комментария.
     */
    private String authorImage;

    /**
     * Имя автора комментария.
     */
    private String authorFirstName;

    /**
     * Время создания комментария в формате Unix timestamp.
     */
    private long createdAt;

    /**
     * Текст комментария.
     */
    private String text;

    /**
     * Пользователь, оставивший комментарий.
     * Связь с сущностью User.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userCom;

    /**
     * Объявление, к которому относится комментарий.
     * Связь с сущностью Ad.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;
}