package ru.skypro.homework.dto;

import ru.skypro.homework.exception.CreateOrUpdateAdException;

public class CreateOrUpdateAd {
    private String title;
    private int price;
    private String description;

    public CreateOrUpdateAd() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws CreateOrUpdateAdException {
        if (title.length() >= 4 && title.length() <= 32) {
            this.title = title;
        } else {
            throw new CreateOrUpdateAdException("title length");
        }
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) throws CreateOrUpdateAdException {
        if (price >= 0 && price <=10000000) {
            this.price = price;
        } else {
            throw new CreateOrUpdateAdException("price value");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws CreateOrUpdateAdException {
        if (description.length() >= 8 && description.length() <=64) {
            this.description = description;
        } else {
            throw new CreateOrUpdateAdException("description length");
        }
    }
}
