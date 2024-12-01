package ru.skypro.homework.dto;

import ru.skypro.homework.exception.UpdateUserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateUser {

    private int firstNameMinimalLength = 3;
    private int firstNameMaximalLength = 10;
    private int lastNameMinimalLength = 3;
    private int lastNameMaximalLength = 10;
    private String phoneNumberRegex = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}";

    private String firstName;
    private String lastName;
    private String phone;

    public UpdateUser() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws UpdateUserException {
        if (firstName.length() >= firstNameMinimalLength && firstName.length() <= firstNameMaximalLength) {
            this.firstName = firstName;
        } else {
            throw new UpdateUserException("first name length");
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws UpdateUserException {
        if (lastName.length() >= lastNameMinimalLength && lastName.length() <= lastNameMaximalLength) {
            this.lastName = lastName;
        } else {
            throw new UpdateUserException("last name length");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws UpdateUserException {
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            this.phone = phone;
        } else {
            throw new UpdateUserException("phone pattern");
        }
    }
}
