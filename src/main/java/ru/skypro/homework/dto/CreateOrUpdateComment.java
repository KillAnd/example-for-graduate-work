package ru.skypro.homework.dto;

import lombok.Getter;
import ru.skypro.homework.exception.CreateOrUpdateCommentException;

/**
 * Класс, представляющий данные для создания или обновления комментария.
 * Содержит ограничения на длину текста комментария и выполняет валидацию
 * при установке значения.
 */
public class CreateOrUpdateComment {

    /**
     * Минимальная длина текста комментария.
     */
    private final int MIN = 8;

    /**
     * Максимальная длина текста комментария.
     */
    private int MAX = 64;

    /**
     * Текст комментария.
     * -- GETTER --
     *  Возвращает текст комментария.
     *
     * @return Текст комментария.

     */
    @Getter
    private String textDTO;

    /**
     * Конструктор по умолчанию.
     */
    public CreateOrUpdateComment() {
    }

    /**
     * Устанавливает текст комментария. Выполняет валидацию длины текста.
     *
     * @param textDTO Текст комментария.
     * @throws CreateOrUpdateCommentException Если длина текста не соответствует ограничениям.
     */
    public void setTextDTO(String textDTO) throws CreateOrUpdateCommentException {
        if (textDTO.length() >= MIN && textDTO.length() <= MAX) {
            this.textDTO = textDTO;
        } else {
            throw new CreateOrUpdateCommentException("text length");
        }
    }
}
