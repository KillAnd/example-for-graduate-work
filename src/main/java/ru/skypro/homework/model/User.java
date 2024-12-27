package ru.skypro.homework.model;

import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс, представляющий сущность "Пользователь".
 * Содержит информацию о пользователе, такую как email, имя, фамилия, телефон, роль, пароль,
 * изображение профиля и список объявлений, созданных пользователем.
 * Является сущностью JPA, связанной с таблицей "app_user" в базе данных.
 */
@Entity
@Table(name = "app_user")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * Email пользователя, используется как логин
     */
    private String email;

    /**
     * Имя пользователя.
     */
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    private String lastName;

    /**
     * Номер телефона пользователя.
     */
    private String phone;

    /**
     * Роль пользователя в системе.
     */
    private Role role;

    /**
     * Изображение профиля пользователя.
     * Связь один-к-одному с сущностью {@link Image}.
     */
    @OneToOne(mappedBy = "user")
    private Image imageUser;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Список объявлений, созданных пользователем.
     * Связь один-ко-многим с сущностью {@link Ad}.
     */
    @OneToMany(mappedBy = "author")
    private List<Ad> ads = new ArrayList<>();


    public User() {
    }

    public User(int id, String email, String firstName, String lastName, String phone,
                Role role, Image imageUser, String password, List<Ad> ads) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.imageUser = imageUser;
        this.password = password;
        this.ads = ads;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Image getImageUser() {
        return imageUser;
    }

    public void setImageUser(Image imageUser) {
        this.imageUser = imageUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Ad> getAds() {
        return ads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phone, user.phone) && role == user.role && Objects.equals(imageUser, user.imageUser) && Objects.equals(password, user.password) && Objects.equals(ads, user.ads);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, phone, role, imageUser, password, ads);
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", imageUser=" + imageUser +
                ", password='" + password + '\'' +
                ", ads=" + ads +
                '}';
    }
}
