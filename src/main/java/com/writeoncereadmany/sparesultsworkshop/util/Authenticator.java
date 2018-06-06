package com.writeoncereadmany.sparesultsworkshop.util;

import co.unruly.control.result.Result;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;

public interface Authenticator {
    boolean oldAuthenticate(Enquiry enquiry);

    Result<Enquiry, String> authenticate(Enquiry enquiry);
}
