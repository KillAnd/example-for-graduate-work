package ru.skypro.homework.dto;

import ru.skypro.homework.exception.NewPasswordException;

public class NewPassword {

    private int passwordMinimalLength = 8;
    private int passwordMaximalLength = 16;

    private String currentPassword;
    private String newPassword;

    public NewPassword() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) throws NewPasswordException {
        if (currentPassword.length() >= passwordMinimalLength && currentPassword.length() <= passwordMaximalLength) {
            this.currentPassword = currentPassword;
        } else {
            throw new NewPasswordException("current password length");
        }
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) throws NewPasswordException {
        if (newPassword.length() >= passwordMinimalLength && newPassword.length() <= passwordMaximalLength) {
            this.newPassword = newPassword;
        } else {
            throw new NewPasswordException("new password length");
        }
    }
}
