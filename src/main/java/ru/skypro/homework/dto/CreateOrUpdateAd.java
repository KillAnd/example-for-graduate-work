package ru.skypro.homework.dto;

import ru.skypro.homework.exception.CreateOrUpdateAdException;

public class CreateOrUpdateAd {

    private int titleMinimalLength = 4;
    private int titleMaximalLength = 32;
    private int priceMinimalValue = 0;
    private int priceMaximalValue = 64;
    private int descriptionMinimalLength = 8;
    private int descriptionMaximalLength = 64;

    private String title;
    private int price;
    private String description;

    public CreateOrUpdateAd() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws CreateOrUpdateAdException {
        if (title.length() >= titleMinimalLength && title.length() <= titleMaximalLength) {
            this.title = title;
        } else {
            throw new CreateOrUpdateAdException("title length");
        }
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) throws CreateOrUpdateAdException {
        if (price >= priceMinimalValue && price <= priceMaximalValue) {
            this.price = price;
        } else {
            throw new CreateOrUpdateAdException("price value");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws CreateOrUpdateAdException {
        if (description.length() >= descriptionMinimalLength && description.length() <= descriptionMaximalLength) {
            this.description = description;
        } else {
            throw new CreateOrUpdateAdException("description length");
        }
    }
}
