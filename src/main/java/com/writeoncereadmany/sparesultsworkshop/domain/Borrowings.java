package com.writeoncereadmany.sparesultsworkshop.domain;

public interface Borrowings {
    enum Withdrawal { AVAILABLE, ALREADY_WITHDRAWN}

    Withdrawal markAsBorrowed(Book toBorrow);

}
