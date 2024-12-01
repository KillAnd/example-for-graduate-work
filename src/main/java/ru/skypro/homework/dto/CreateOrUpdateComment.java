package ru.skypro.homework.dto;

import ru.skypro.homework.exception.CreateOrUpdateCommentException;

public class CreateOrUpdateComment {

    private String text;

    public CreateOrUpdateComment() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) throws CreateOrUpdateCommentException {
        if (text.length() >= 8 && text.length() <=64) {
            this.text = text;
        } else {
            throw new CreateOrUpdateCommentException("text length");
        }
    }
}
