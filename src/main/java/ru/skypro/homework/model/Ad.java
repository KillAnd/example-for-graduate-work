package ru.skypro.homework.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс, представляющий сущность "Объявление".
 * Содержит информацию об объявлении, такую как цена, заголовок, описание, изображение, автор и комментарии.
 * Является сущностью JPA, связанной с таблицей в базе данных.
 */
@Entity
public class Ad {

    /**
     * Уникальный идентификатор объявления.
     */
    @Id
    @GeneratedValue
    private int adId;

    /**
     * Цена объявления.
     */
    private int price;

    /**
     * Заголовок объявления.
     */
    private String title;

    /**
     * Описание объявления.
     */
    private String description;

    /**
     * Изображение, связанное с объявлением.
     * Связь один-к-одному с сущностью {@link Image}.
     */
    @OneToOne(mappedBy = "ad")
    private Image imageAd;

    /**
     * Автор объявления.
     * Связь многие-к-одному с сущностью {@link User}.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    /**
     * Список комментариев, связанных с объявлением.
     * Связь один-ко-многим с сущностью {@link Comment}.
     */
    @OneToMany(mappedBy = "ad")
    private List<Comment> comments = new ArrayList<>();


    public Ad() {
    }

    public Ad(User author, Image imageAd, int adId, int price, String title, String description, List<Comment> comments) {
        this.author = author;
        this.imageAd = imageAd;
        this.adId = adId;
        this.price = price;
        this.title = title;
        this.description = description;
        this.comments = comments;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Image getImageAd() {
        return imageAd;
    }

    public void setImageAd(Image imageAd) {
        this.imageAd = imageAd;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return adId == ad.adId && price == ad.price && Objects.equals(author, ad.author) && Objects.equals(imageAd, ad.imageAd) && Objects.equals(title, ad.title) && Objects.equals(description, ad.description) && Objects.equals(comments, ad.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, imageAd, adId, price, title, description, comments);
    }

    @Override
    public String toString() {
        return "Ad{" +
                "author=" + author +
                ", imageAd=" + imageAd +
                ", pk=" + adId +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", comments=" + comments +
                '}';
    }
}
