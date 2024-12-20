package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

import java.awt.*;
import java.util.Optional;

public interface UserService {


    boolean checkCurrentPassword(String email, String currentPassword);
    void updatePassword(String email, NewPassword newPassword);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    User updateUser(String email, UpdateUser updateUser);
    void updateUserImage(String email, MultipartFile image);


}
