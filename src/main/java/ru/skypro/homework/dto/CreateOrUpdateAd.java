package ru.skypro.homework.dto;

import lombok.Getter;
import ru.skypro.homework.exception.CreateOrUpdateAdException;

/**
 * Класс, представляющий данные для создания или обновления объявления.
 * Содержит ограничения на длину заголовка, описание и диапазон цены,
 * а также поля для хранения данных объявления. Включает валидацию данных
 * при установке значений.
 */
public class CreateOrUpdateAd {

    /**
     * Минимальная длина заголовка объявления.
     */
    private final int TITLE_MIN_LENGTH = 4;

    /**
     * Максимальная длина заголовка объявления.
     */
    private final int TITLE_MAX_LENGTH = 32;

    /**
     * Минимальное значение цены объявления.
     */
    private final int PRICE_MIN_VALUE = 0;

    /**
     * Максимальное значение цены объявления.
     */
    private final int PRICE_MAX_VALUE = 64;

    /**
     * Минимальная длина описания объявления.
     */
    private final int DESCRIPTION_MIN_LENGTH = 8;

    /**
     * Максимальная длина описания объявления.
     */
    private final int DESCRIPTION_MAX_LENGTH = 64;

    /**
     * Заголовок объявления.
     * -- GETTER --
     *  Возвращает заголовок объявления.
     *
     * @return Заголовок объявления.

     */
    @Getter
    private String title;

    /**
     * Цена объявления.
     * -- GETTER --
     *  Возвращает цену объявления.
     *
     * @return Цена объявления.

     */
    @Getter
    private int price;

    /**
     * Описание объявления.
     * -- GETTER --
     *  Возвращает описание объявления.
     *
     * @return Описание объявления.

     */
    @Getter
    private String description;

    /**
     * Конструктор по умолчанию.
     */
    public CreateOrUpdateAd() {
    }

    /**
     * Устанавливает заголовок объявления. Выполняет валидацию длины заголовка.
     *
     * @param title Заголовок объявления.
     * @throws CreateOrUpdateAdException Если длина заголовка не соответствует ограничениям.
     */
    public void setTitle(String title) throws CreateOrUpdateAdException {
        if (title.length() >= TITLE_MIN_LENGTH && title.length() <= TITLE_MAX_LENGTH) {
            this.title = title;
        } else {
            throw new CreateOrUpdateAdException("title length");
        }
    }

    /**
     * Устанавливает цену объявления. Выполняет валидацию значения цены.
     *
     * @param price Цена объявления.
     * @throws CreateOrUpdateAdException Если значение цены не соответствует ограничениям.
     */
    public void setPrice(int price) throws CreateOrUpdateAdException {
        if (price >= PRICE_MIN_VALUE && price <= PRICE_MAX_VALUE) {
            this.price = price;
        } else {
            throw new CreateOrUpdateAdException("price value");
        }
    }

    /**
     * Устанавливает описание объявления. Выполняет валидацию длины описания.
     *
     * @param description Описание объявления.
     * @throws CreateOrUpdateAdException Если длина описания не соответствует ограничениям.
     */
    public void setDescription(String description) throws CreateOrUpdateAdException {
        if (description.length() >= DESCRIPTION_MIN_LENGTH && description.length() <= DESCRIPTION_MAX_LENGTH) {
            this.description = description;
        } else {
            throw new CreateOrUpdateAdException("description length");
        }
    }
}