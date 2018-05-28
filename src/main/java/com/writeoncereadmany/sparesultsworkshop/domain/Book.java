package com.writeoncereadmany.sparesultsworkshop.domain;

public class Book {

    public final String isbn;
    public final String title;
    public final String author;
    public final String content;

    public Book(String isbn, String title, String author, String content) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.content = content;
    }
}
