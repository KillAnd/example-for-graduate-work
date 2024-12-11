package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Register {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
