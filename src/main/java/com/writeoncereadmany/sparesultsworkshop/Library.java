package com.writeoncereadmany.sparesultsworkshop;

import com.writeoncereadmany.sparesultsworkshop.domain.Book;
import com.writeoncereadmany.sparesultsworkshop.domain.Books;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;
import com.writeoncereadmany.sparesultsworkshop.util.Authenticator;
import com.writeoncereadmany.sparesultsworkshop.util.ObjectMapper;

import java.util.Map;

import static co.unruly.control.Piper.pipe;

public class Library {

    private final ObjectMapper mapper;
    private final Authenticator authenticator;
    private final Books books;
    private final Borrowings borrowings;

    public Library(
        ObjectMapper mapper,
        Authenticator authenticator,
        Map<String, Book> books,
        Borrowings borrowings
    ) {
        this.mapper = mapper;
        this.authenticator = authenticator;
        this.books = new Books(books);
        this.borrowings = borrowings;
    }

    public String borrow(String request) {
        return pipe(request)
            // we need some steps in the middle here
            .resolve();
    }

    /**
     * We leave the previous approach here to remind us what we need to do,
     * and so we have it as a basis for comparison at the end of the conversion
     */
    public String traditionalBorrow(String request) {
        Enquiry enquiry = mapper.readObject(request);
        Book book = books.get(enquiry);
        borrowings.markAsBorrowed(book);
        return book.getContent();
    }
}
