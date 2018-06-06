package com.writeoncereadmany.sparesultsworkshop.domain;

import co.unruly.control.Optionals;
import co.unruly.control.result.Result;

import java.util.Map;
import java.util.Optional;

public class Books {

    private final Map<String, Book> books;

    public Books(Map<String, Book> books) {
        this.books = books;
    }

    public Book oldGet(Enquiry enquiry) {
        return books.get(enquiry.getIsbn());
    }

    public Result<Book, String> get(Enquiry enquiry) {
        return Optionals.either(
            Optional.ofNullable(books.get(enquiry.getIsbn())),
            book -> Result.success(book),
            () -> Result.failure("Book not found")
        );
    }
}
