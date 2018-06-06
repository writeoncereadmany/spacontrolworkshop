package com.writeoncereadmany.sparesultsworkshop.util;

import co.unruly.control.result.Result;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;

public interface ObjectMapper {

    Enquiry oldReadObject(String representation);

    Result<Enquiry, String> readObject(String representation);
}
