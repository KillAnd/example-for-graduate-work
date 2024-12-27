package ru.skypro.homework.model;

import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //id пользователя
    private String username; //логин пользователя
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private String phone;
    private String image; //ссылка на его аватар

    @OneToMany(mappedBy = "userAd", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ad> adsListForUsers;

    @OneToMany(mappedBy = "userCom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentsListForUsers;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_image_id", referencedColumnName = "id")
    private Image imageUsers;
}
