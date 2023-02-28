package com.example.myonlinebookself.items;

public class Book {
    String id;
    String title;
    String author;
    String description;
    String details;
    int image;

    public Book(String id, String title, String author, String description, String details, int image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.details = details;
        this.image = image;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getId() {return this.id; }
}