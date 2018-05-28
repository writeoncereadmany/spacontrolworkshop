package com.writeoncereadmany.sparesultsworkshop.util;

public interface ObjectMapper {

    <T> T readObject(String representation, Class<T> targetClass);
}
