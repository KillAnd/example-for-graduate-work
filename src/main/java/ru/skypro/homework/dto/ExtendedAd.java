package ru.skypro.homework.dto;

import ru.skypro.homework.model.Image;

/**
 * Класс, представляющий расширенную информацию об объявлении.
 * Содержит данные об авторе, изображении, цене, заголовке, описании и контактной информации.
 */
public class ExtendedAd {

    /**
     * Уникальный идентификатор объявления.
     */
    private int adId;

    /**
     * Имя автора объявления.
     */
    private String authorFirstName;

    /**
     * Фамилия автора объявления.
     */
    private String authorLastName;

    /**
     * Описание объявления.
     */
    private String description;

    /**
     * Email автора объявления.
     */
    private String email;

    /**
     * Изображение, связанное с объявлением.
     */
    private Image image;

    /**
     * Номер телефона автора объявления.
     */
    private String phone;

    /**
     * Цена объявления.
     */
    private int price;

    /**
     * Заголовок объявления.
     */
    private String title;


    public ExtendedAd() {
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
