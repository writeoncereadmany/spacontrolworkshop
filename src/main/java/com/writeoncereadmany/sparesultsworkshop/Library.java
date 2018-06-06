package com.writeoncereadmany.sparesultsworkshop;

import com.writeoncereadmany.sparesultsworkshop.domain.Book;
import com.writeoncereadmany.sparesultsworkshop.domain.Books;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings.Withdrawal;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;
import com.writeoncereadmany.sparesultsworkshop.util.Authenticator;
import com.writeoncereadmany.sparesultsworkshop.util.ObjectMapper;

import java.util.Map;

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
        try {
            Enquiry enquiry = mapper.readObject(request);
            if(!authenticator.authenticate(enquiry)) {
                return "Unauthorized";
            }
            Book book = books.get(enquiry);
            if(book == null) {
                return "Cannot find book";
            }
            Withdrawal withdrawalStatus = borrowings.markAsBorrowed(book);
            if(withdrawalStatus == Withdrawal.ALREADY_WITHDRAWN) {
                return "Book already withdrawn";
            }
            return book.getContent();
        } catch (RuntimeException ex) {
            return "Malformed request";
        }
    }

    /**
     * We leave this here so we can compare our error-handling implementation against the original
     */
    public String borrowWithoutErrorHandling(String request) {
        Enquiry enquiry = mapper.readObject(request);
        Book book = books.get(enquiry);
        borrowings.markAsBorrowed(book);
        return book.getContent();
    }
}
