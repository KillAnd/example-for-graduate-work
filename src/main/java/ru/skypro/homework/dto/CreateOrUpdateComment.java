package ru.skypro.homework.dto;

import ru.skypro.homework.exception.CreateOrUpdateCommentException;

public class CreateOrUpdateComment {

    private int textMinimalLength = 8;
    private int TextMaximalLength = 64;

    private String textDTO;

    public CreateOrUpdateComment() {
    }

    public String getTextDTO() {
        return textDTO;
    }

    public void setTextDTO(String textDTO) throws CreateOrUpdateCommentException {
        if (textDTO.length() >= textMinimalLength && textDTO.length() <= TextMaximalLength) {
            this.textDTO = textDTO;
        } else {
            throw new CreateOrUpdateCommentException("text length");
        }
    }
}
