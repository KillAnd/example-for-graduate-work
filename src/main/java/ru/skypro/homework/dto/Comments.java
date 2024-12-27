package ru.skypro.homework.dto;

import ru.skypro.homework.model.Comment;

import java.util.List;

/**
 * Класс, представляющий коллекцию комментариев.
 * Содержит общее количество комментариев и список самих комментариев.
 */
public class Comments {

    /**
     * Общее количество комментариев.
     */
    private int count;

    /**
     * Список комментариев.
     */
    private List<Comment> results;


    public Comments() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Comment> getResults() {
        return results;
    }

    public void setResults(List<Comment> results) {
        this.results = results;
    }
}
