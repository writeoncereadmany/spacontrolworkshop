package com.writeoncereadmany.sparesultsworkshop.domain;

import java.util.Map;

public class Books {

    private final Map<String, Book> books;

    public Books(Map<String, Book> books) {
        this.books = books;
    }

    public Book get(Enquiry enquiry) {
        return books.get(enquiry.getIsbn());
    }
}
