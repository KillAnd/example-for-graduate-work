package ru.skypro.homework.dto;

import ru.skypro.homework.exception.CreateOrUpdateCommentException;

public class CreateOrUpdateComment {

    private int textMinimalLength = 8;
    private int TextMaximalLength = 64;

    private String text;

    public CreateOrUpdateComment() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) throws CreateOrUpdateCommentException {
        if (text.length() >= textMinimalLength && text.length() <= TextMaximalLength) {
            this.text = text;
        } else {
            throw new CreateOrUpdateCommentException("text length");
        }
    }
}
