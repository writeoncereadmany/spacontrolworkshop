package com.writeoncereadmany.sparesultsworkshop.domain;

import co.unruly.control.Optionals;
import co.unruly.control.result.Result;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;

public class Books {

    private final Map<String, List<Book>> books;

    public Books(Map<String, List<Book>> books) {
        this.books = books;
    }

    public List<Book> getAll(Enquiry enquiry) { return books.get(enquiry.getIsbn()); }

    @Deprecated
    public Book get(Enquiry enquiry) {
        List<Book> matches = this.books.get(enquiry.getIsbn());
        if(null == matches || matches.isEmpty()) {
            return null;
        }
        return matches.get(0);
    }

    @Deprecated
    public Result<Book, String> newGet(Enquiry enquiry) {
        return Optionals.either(
            Optional.ofNullable(get(enquiry)),
            book -> Result.success(book),
            () -> Result.failure("Book not found")
        );
    }
}
