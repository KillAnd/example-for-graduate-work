package ru.skypro.homework.model;

import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class User {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    @OneToMany(mappedBy = "user")
    private ImageDTO imageUser;
    private String password;

    @OneToMany(mappedBy = "author")
    private List<Ad> ads;

    public User() {
    }

    public User(int id, String email, String firstName, String lastName, String phone, Role role, ImageDTO imageUser) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.imageUser = imageUser;
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

    public ImageDTO getImageUser() {
        return imageUser;
    }

    public void setImageUser(ImageDTO imageUser) {
        this.imageUser = imageUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
