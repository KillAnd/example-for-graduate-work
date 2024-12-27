package ru.skypro.homework.dto;
import ru.skypro.homework.model.Ad;

import java.util.List;

/**
 * Класс, представляющий коллекцию объявлений.
 * Содержит общее количество объявлений и список самих объявлений.
 */
public class Ads {

    /**
     * Общее количество объявлений.
     */
    private int count;

    /**
     * Список объявлений.
     */
    private List<Ad> results;


    public Ads() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Ad> getResults() {
        return results;
    }

    public void setResults(List<Ad> results) {
        this.results = results;
    }
}
