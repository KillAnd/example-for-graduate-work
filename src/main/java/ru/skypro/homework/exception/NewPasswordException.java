package ru.skypro.homework.exception;

public class NewPasswordException extends RuntimeException{
    public NewPasswordException(String message) {
        super(message);
    }
}
