package com.writeoncereadmany.sparesultsworkshop;

import co.unruly.control.Piper;
import co.unruly.control.result.Result;
import com.writeoncereadmany.sparesultsworkshop.domain.Book;
import com.writeoncereadmany.sparesultsworkshop.domain.Books;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;
import com.writeoncereadmany.sparesultsworkshop.util.Authenticator;
import com.writeoncereadmany.sparesultsworkshop.util.ObjectMapper;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static co.unruly.control.Piper.pipe;
import static co.unruly.control.result.Introducers.ifEquals;
import static co.unruly.control.result.Introducers.tryTo;
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

    private static <S, F> Function<S, Result<S, F>> ifFalse(Predicate<S> test, F failure) {
        return val -> test.test(val) ? success(val) : failure(failure);
    }

    private static <S, S1, F> Function<S, Result<S1, F>> ifNull(Function<S, S1> mapper, F failure) {
        return input -> {
            final S1 output = mapper.apply(input);
            return output == null ? failure(failure) : success(output);
        };
    }

    private static <S, F, V> Function<S, Result<S, F>> ifYields(Function<S, V> checker, V value, F failure) {
        return input -> checker.apply(input) == value ? success(input) : failure(failure);
    }
}
