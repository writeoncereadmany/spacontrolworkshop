package com.writeoncereadmany.sparesultsworkshop.util;

public interface Authenticator {
    boolean authenticate(String username, String password);
}
