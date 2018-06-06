package com.writeoncereadmany.sparesultsworkshop.util;

import com.writeoncereadmany.sparesultsworkshop.domain.Enquiry;

public interface ObjectMapper {

    Enquiry readObject(String representation);
}
