package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

import java.awt.*;
import java.util.Optional;

public interface UserService {


    boolean checkCurrentPassword(String username, String currentPassword);
    void updatePassword(String username, NewPassword newPassword);

    Optional<User> findUserById(Integer id);
    User findUserByUsername(String username);

    UpdateUser updateUser(String username, UpdateUser updateUser);
    void updateUserImage(String username, MultipartFile image);


}
