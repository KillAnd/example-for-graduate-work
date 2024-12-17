package ru.skypro.homework.dto;
import ru.skypro.homework.model.Ad;

import java.util.List;

public class Ads {

    private int count;
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
