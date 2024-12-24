package ru.skypro.homework.model;

import ru.skypro.homework.dto.ImageDTO;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ad {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private int author;
    @OneToMany(mappedBy = "ad")
    private ImageDTO imageAd;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Id
    @GeneratedValue
    private int pk;
    private int price;
    private String title;
    private String description;

    @OneToMany(mappedBy = "ad")
    private List<Comment> comments;

    public Ad() {
    }

    public Ad(int author, ImageDTO imageAd, User user, int pk, int price, String title, String description, List<Comment> comments) {
        this.author = author;
        this.imageAd = imageAd;
        this.user = user;
        this.pk = pk;
        this.price = price;
        this.title = title;
        this.description = description;
        this.comments = comments;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public ImageDTO getImageAd() {
        return imageAd;
    }

    public void setImageAd(ImageDTO imageAd) {
        this.imageAd = imageAd;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
