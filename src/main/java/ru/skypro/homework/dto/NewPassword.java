package ru.skypro.homework.dto;

import ru.skypro.homework.exception.NewPasswordException;

public class NewPassword {

    private String currentPassword;
    private String newPassword;

    public NewPassword() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) throws NewPasswordException {
        if (currentPassword.length() >= 8 && currentPassword.length() <=16) {
            this.currentPassword = currentPassword;
        } else {
            throw new NewPasswordException("current password length");
        }
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) throws NewPasswordException {
        if (newPassword.length() >= 8 && newPassword.length() <= 16) {
            this.newPassword = newPassword;
        } else {
            throw new NewPasswordException("new password length");
        }
    }
}
