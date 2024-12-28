package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@ToString(exclude = {"author", "userAd", "comments", "imageAd"})
public class Ad {

    @Id
    @GeneratedValue
    private int pk;
    private int author;
    private int price;
    private String title;
    private String image;
    private String description;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userAd; //связь user

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_image_id", referencedColumnName = "id")
    private Image imageAd;
}
