package com.writeoncereadmany.sparesultsworkshop.domain;

import co.unruly.control.result.Result;

public interface Borrowings {
    enum Withdrawal { AVAILABLE, ALREADY_WITHDRAWN}

    Withdrawal markAsBorrowed(Book toBorrow);

    Result<Book, String> borrow2(Book toBorrow);

}
