package com.writeoncereadmany.sparesultsworkshop;

import co.unruly.control.result.Result;
import com.writeoncereadmany.sparesultsworkshop.domain.Book;
import com.writeoncereadmany.sparesultsworkshop.domain.Books;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;
import com.writeoncereadmany.sparesultsworkshop.util.Authenticator;
import com.writeoncereadmany.sparesultsworkshop.util.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static co.unruly.control.Piper.pipe;
import static co.unruly.control.result.Introducers.*;
import static co.unruly.control.result.Resolvers.collapse;
import static co.unruly.control.result.Result.failure;
import static co.unruly.control.result.Result.success;
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
        Map<String, List<Book>> books,
        Borrowings borrowings
    ) {
        this.mapper = mapper;
        this.authenticator = authenticator;
        this.books = new Books(books);
        this.borrowings = borrowings;
    }

    public String borrow(String request) {
        return pipe(request)
            .then(tryTo(mapper::readObject, __ -> "Malformed request"))
            .then(attempt(ifFalse(authenticator::authenticate, "Unauthorized")))
            .then(attempt(ifSingle(books::getAll, "Cannot find book", "Book ISBN ambiguous")))
            .then(attempt(ifYields(borrowings::markAsBorrowed, ALREADY_WITHDRAWN, "Book already withdrawn")))
            .then(onSuccess(Book::getContent))
            .then(collapse())
            .resolve();
    }

    private Function<Enquiry, Result<Book, String>> getSingleBook() {
        return enquiry -> {
            List<Book> booksFound = books.getAll(enquiry);
            if(booksFound == null || booksFound.isEmpty()) {
                return failure("Cannot find book");
            }
            else if(booksFound.size() > 1) {
                return failure("Book ISBN ambiguous");
            }
            else {
                return success(booksFound.get(0));
            }
        };
    }

    private static <I, S, F> Function<I, Result<S, F>> ifSingle(
        Function<I, List<S>> f,
        F ifNoneFound,
        F ifMultiplesFound
    ) {
        return input -> {
            List<S> items = f.apply(input);
            if(items == null || items.isEmpty()) {
                return failure(ifNoneFound);
            }
            else if(items.size() > 1) {
                return failure(ifMultiplesFound);
            }
            else {
                return success(items.get(0));
            }
        };
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
