package com.writeoncereadmany.sparesultsworkshop;

import com.writeoncereadmany.sparesultsworkshop.domain.Book;
import com.writeoncereadmany.sparesultsworkshop.domain.Books;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings;
import com.writeoncereadmany.sparesultsworkshop.util.Authenticator;
import com.writeoncereadmany.sparesultsworkshop.util.ObjectMapper;

import java.util.Map;

import static co.unruly.control.Piper.pipe;
import static co.unruly.control.result.Introducers.*;
import static co.unruly.control.result.Resolvers.collapse;
import static co.unruly.control.result.Transformers.attempt;
import static co.unruly.control.result.Transformers.onSuccess;
import static com.writeoncereadmany.sparesultsworkshop.domain.Borrowings.Withdrawal.ALREADY_WITHDRAWN;

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
            .then(mapper::readObject2)
            .then(attempt(authenticator::authenticate2))
            .then(attempt(books::get2))
            .then(attempt(borrowings::borrow2))
            .then(onSuccess(Book::getContent))
            .then(collapse())
            .resolve();
    }

    public String borrow2(String request) {
        return pipe(request)
            .then(tryTo(mapper::readObject, __ -> "Cannot parse request"))
            .then(attempt(ifFalse(authenticator::authenticate, "Not authorized")))
            .then(attempt(ifNull(books::get, "Book not found")))
            .then(attempt(ifYields(borrowings::markAsBorrowed, ALREADY_WITHDRAWN, "Book already withdrawn")))
            .then(onSuccess(Book::getContent))
            .resolveWith(collapse());
    }
}
