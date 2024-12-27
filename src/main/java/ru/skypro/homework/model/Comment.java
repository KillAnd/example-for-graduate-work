package ru.skypro.homework.model;

import javax.persistence.*;

/**
 * Класс, представляющий сущность "Комментарий".
 * Содержит информацию о комментарии, такую как автор, изображение автора, имя автора,
 * дата создания, текст комментария и связанное объявление.
 * Является сущностью JPA, связанной с таблицей в базе данных.
 */
@Entity
public class Comment {

    /**
     * Идентификатор автора комментария.
     */
    private int author;

    /**
     * Ссылка на изображение автора комментария.
     */
    private String authorImage;

    /**
     * Имя автора комментария.
     */
    private String authorFirstName;

    /**
     * Временная метка создания комментария (в миллисекундах).
     */
    private long createdAt;

    /**
     * Уникальный идентификатор комментария.
     */
    @Id
    @GeneratedValue
    private int commentId;

    /**
     * Текст комментария.
     */
    private String text;

    /**
     * Объявление, к которому относится комментарий.
     * Связь многие-к-одному с сущностью {@link Ad}.
     */
    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;


    public Comment() {
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }
}
