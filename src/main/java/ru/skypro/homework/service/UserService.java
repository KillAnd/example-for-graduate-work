package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

import java.awt.*;

public interface UserService {


    boolean checkCurrentPassword(String email, String currentPassword)
    void updatePassword(String email, NewPassword newPassword);

    User findUserByEmail(String email);

    User findUserById(String id);

    User updateUser(String email, UpdateUser updateUser);
    void updateUserImage(String email, MultipartFile image);


}
