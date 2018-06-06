package com.writeoncereadmany.sparesultsworkshop;

import co.unruly.control.Piper;
import com.writeoncereadmany.sparesultsworkshop.domain.Book;
import com.writeoncereadmany.sparesultsworkshop.domain.Books;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;
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

    public String naiveBorrow(String request) {
        Enquiry enquiry = mapper.readObject(request);
        Book book = books.get(enquiry);
        borrowings.markAsBorrowed(book);
        return book.getContent();
    }

    public String pipeBorrowNoErrorHandling(String request) {
        return pipe(request)
            .then(mapper::readObject)
            .then(books::get)
            .peek(borrowings::markAsBorrowed)
            .then(Book::getContent)
            .resolve();
    }

    public String borrowWithTraditionalErrorHandling(String request) {
        try {
            Enquiry enquiry = mapper.readObject(request);
            if(!authenticator.authenticate(enquiry)) {
                return "Unauthorized";
            }
            Book book = books.get(enquiry);
            if(book == null) {
                return "Cannot find book";
            }
            Borrowings.Withdrawal withdrawalStatus = borrowings.markAsBorrowed(book);
            if(withdrawalStatus == ALREADY_WITHDRAWN) {
                return "Book already withdrawn";
            }
            return book.getContent();
        } catch (RuntimeException ex) {
            return "Malformed request";
        }
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

    public String borrowWithFunctionalErrorHandling(String request) {
        return pipe(request)
            .then(tryTo(mapper::readObject, __ -> "Malformed request"))
            .then(attempt(ifFalse(authenticator::authenticate, "Unauthorized")))
            .then(attempt(ifNull(books::get, "Cannot find book")))
            .then(attempt(ifYields(borrowings::markAsBorrowed, ALREADY_WITHDRAWN, "Book already withdrawn")))
            .then(onSuccess(Book::getContent))
            .resolveWith(collapse());
    }
}
