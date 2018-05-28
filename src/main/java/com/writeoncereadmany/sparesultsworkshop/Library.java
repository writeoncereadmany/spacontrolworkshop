package com.writeoncereadmany.sparesultsworkshop;

import com.writeoncereadmany.sparesultsworkshop.domain.Book;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings;
import com.writeoncereadmany.sparesultsworkshop.util.Authenticator;
import com.writeoncereadmany.sparesultsworkshop.util.ObjectMapper;

import java.util.Map;

public class Library {

    private final ObjectMapper mapper;
    private final Authenticator authenticator;
    private final Map<String, Book> books;
    private final Borrowings borrowings;

    public Library(
        ObjectMapper mapper,
        Authenticator authenticator,
        Map<String, Book> books,
        Borrowings borrowings
    ) {
        this.mapper = mapper;
        this.authenticator = authenticator;
        this.books = books;
        this.borrowings = borrowings;
    }

    public String borrow(String request) {
        final Enquiry enquiry = mapper.readObject(request, Enquiry.class);
        final Book book = books.get(enquiry.isbn);
        borrowings.markAsBorrowed(book);
        return book.content;
    }
}
