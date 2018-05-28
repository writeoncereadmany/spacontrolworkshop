package com.writeoncereadmany.sparesultsworkshop.domain;

public class Enquiry {
    public final String username;
    public final String password;
    public final String isbn;

    public Enquiry(String username, String password, String isbn) {
        this.username = username;
        this.password = password;
        this.isbn = isbn;
    }
}
