package com.writeoncereadmany.sparesultsworkshop.domain;

public class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private final String content;

    public Book(String isbn, String title, String author, String content) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
