package com.writeoncereadmany.sparesultsworkshop;

import com.writeoncereadmany.sparesultsworkshop.domain.Book;
import com.writeoncereadmany.sparesultsworkshop.domain.Books;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings;
import com.writeoncereadmany.sparesultsworkshop.util.Authenticator;
import com.writeoncereadmany.sparesultsworkshop.util.ObjectMapper;

import java.util.Map;

import static co.unruly.control.Piper.pipe;
import static co.unruly.control.result.Resolvers.collapse;
import static co.unruly.control.result.Transformers.attempt;
import static co.unruly.control.result.Transformers.onSuccess;

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
            // We need to do various transformations on our data here
            .resolve();
    }

    /**
     * We keep the previous implementation for reference, and for comparison once we're done
     */
    public String borrowUsingModifiedApis(String request) {
        return pipe(request)
            .then(mapper::newReadObject)
            .then(attempt(authenticator::newAuthenticate))
            .then(attempt(books::newGet))
            .then(attempt(borrowings::newBorrow))
            .then(onSuccess(Book::getContent))
            .then(collapse())
            .resolve();
    }
}
