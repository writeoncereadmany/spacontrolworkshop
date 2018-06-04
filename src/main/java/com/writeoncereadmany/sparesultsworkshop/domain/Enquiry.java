package com.writeoncereadmany.sparesultsworkshop.domain;

public class Enquiry {

    private final String username;
    private final String password;
    private final String isbn;

    public Enquiry(String username, String password, String isbn) {
        this.username = username;
        this.password = password;
        this.isbn = isbn;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIsbn() {
        return isbn;
    }
}
