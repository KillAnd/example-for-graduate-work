package ru.skypro.homework.dto;

public class Comments {

    private int count;
    private Comment[] results;

    public Comments() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Comment[] getResults() {
        return results;
    }

    public void setResults(Comment[] results) {
        this.results = results;
    }
}
