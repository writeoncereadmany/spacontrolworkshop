package com.writeoncereadmany.sparesultsworkshop.util;

import co.unruly.control.result.Result;
import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;

public interface ObjectMapper {

    Enquiry readObject(String representation);

    Result<Enquiry, String> readObject2(String representation);
}
