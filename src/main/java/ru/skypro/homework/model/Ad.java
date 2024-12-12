package ru.skypro.homework.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ad {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private int author;
    private String image;
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

    public Ad(int author, String image, int pk, int price, String title) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.price = price;
        this.title = title;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
