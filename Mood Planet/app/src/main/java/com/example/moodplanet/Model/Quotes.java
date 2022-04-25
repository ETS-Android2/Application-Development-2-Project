package com.example.moodplanet.Model;

public class Quotes {
    String text;
    String author;

    public Quotes(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Quotes{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
