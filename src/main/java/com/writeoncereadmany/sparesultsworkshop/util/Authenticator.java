package com.writeoncereadmany.sparesultsworkshop.util;

import co.unruly.control.result.Result;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;

public interface Authenticator {
    boolean authenticate(Enquiry enquiry);

    @Deprecated
    Result<Enquiry, String> newAuthenticate(Enquiry enquiry);
}
