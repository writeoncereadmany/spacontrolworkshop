package com.writeoncereadmany.sparesultsworkshop.util;

import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;

public interface Authenticator {
    boolean authenticate(Enquiry enquiry);
}
