package com.writeoncereadmany.sparesultsworkshop;

import com.writeoncereadmany.sparesultsworkshop.domain.Book;
import com.writeoncereadmany.sparesultsworkshop.domain.Borrowings;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;
import com.writeoncereadmany.sparesultsworkshop.util.Authenticator;
import com.writeoncereadmany.sparesultsworkshop.util.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static co.unruly.control.pair.Maps.entry;
import static co.unruly.control.pair.Maps.mapOf;
import static com.writeoncereadmany.sparesultsworkshop.domain.Borrowings.Withdrawal.ALREADY_WITHDRAWN;
import static com.writeoncereadmany.sparesultsworkshop.domain.Borrowings.Withdrawal.AVAILABLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LibraryTest {

    private static final Book book = new Book("123", "How To Cook For Forty Humans", "Kang", "lots of words");
    public static final Enquiry enquiry = new Enquiry("Tom", "letmein", "123");

    private final ObjectMapper mapper = mock(ObjectMapper.class);
    private final Authenticator auth = mock(Authenticator.class);
    private final Map<String, Book> books = mapOf(entry(book.getIsbn(), book));
    private final Borrowings borrowings = mock(Borrowings.class);

    private final Library library = new Library(mapper, auth, books, borrowings);

    @Test
    public void canBorrowABook() {
        given(mapper.readObject(anyString())).willReturn(enquiry);
        given(auth.authenticate(enquiry)).willReturn(true);
        given(borrowings.markAsBorrowed(book)).willReturn(AVAILABLE);

        assertThat(
            library.functionalBorrow("{ \"user\": \"Tom\", \"password\": \"letmein\", \"isbn\": \"123\" }"),
            is("lots of words"));

        verify(borrowings).markAsBorrowed(book);
    }

    @Test
    public void reportsMalformedRequest() {
        given(mapper.readObject(anyString())).willThrow(new RuntimeException("Cannot parse object"));

        assertThat(
            library.functionalBorrow("i would like a book plz kthxbai"),
            is("Malformed request"));
    }

    @Test
    public void rejectsUnauthorizedUser() {
        given(mapper.readObject(anyString())).willReturn(enquiry);
        given(auth.authenticate(enquiry)).willReturn(false);

        assertThat(
            library.functionalBorrow("{ \"user\": \"Tom\", \"password\": \"letmein\", \"isbn\": \"123\" }"),
            is("Unauthorized"));
    }

    @Test
    public void cannotLendUnknownBook() {
        Enquiry enquiry = new Enquiry("Tom", "letmein", "456");
        given(mapper.readObject(anyString())).willReturn(enquiry);
        given(auth.authenticate(enquiry)).willReturn(true);

        assertThat(
            library.functionalBorrow("{ \"user\": \"Tom\", \"password\": \"letmein\", \"isbn\": \"456\" }"),
            is("Cannot find book"));
    }

    @Test
    public void cannotLendBookIfAlreadyWithdrawn() {
        given(mapper.readObject(anyString())).willReturn(enquiry);
        given(auth.authenticate(enquiry)).willReturn(true);
        given(borrowings.markAsBorrowed(book)).willReturn(ALREADY_WITHDRAWN);

        assertThat(
            library.functionalBorrow("{ \"user\": \"Tom\", \"password\": \"letmein\", \"isbn\": \"123\" }"),
            is("Book already withdrawn"));

    }
}
