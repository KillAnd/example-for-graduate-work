package ru.skypro.homework.dto;

import ru.skypro.homework.exception.UpdateUserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateUser {
    private String firstName;
    private String lastName;
    private String phone;

    public UpdateUser() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws UpdateUserException {
        if (firstName.length() >= 3 && firstName.length() <=10) {
            this.firstName = firstName;
        } else {
            throw new UpdateUserException("first name length");
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws UpdateUserException {
        if (lastName.length() >= 3 && lastName.length() <=10) {
            this.lastName = lastName;
        } else {
            throw new UpdateUserException("last name length");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws UpdateUserException {
        Pattern pattern = Pattern.compile("\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            this.phone = phone;
        } else {
            throw new UpdateUserException("phone pattern");
        }
    }
}
