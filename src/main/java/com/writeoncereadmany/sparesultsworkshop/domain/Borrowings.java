package com.writeoncereadmany.sparesultsworkshop.domain;

import co.unruly.control.result.Result;

public interface Borrowings {
    enum Withdrawal { AVAILABLE, ALREADY_WITHDRAWN}

    Withdrawal oldMarkAsBorrowed(Book toBorrow);

    Result<Book, String> markAsBorrowed(Book toBorrow);

}
