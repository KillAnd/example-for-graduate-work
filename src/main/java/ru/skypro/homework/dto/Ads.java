package ru.skypro.homework.dto;

public class Ads {

    private int count;
    private Ad[] results;

    public Ads() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Ad[] getResults() {
        return results;
    }

    public void setResults(Ad[] results) {
        this.results = results;
    }
}
