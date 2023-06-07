package com.hyfly.template.sdk.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;

public class TypeReferenceHttpResult<T> extends TypeReference<BaseHttpRestResult<T>> {

    protected final Type type;

    public TypeReferenceHttpResult(Class<?>... clazz) {
        ObjectMapper mapper = new ObjectMapper();
        type = mapper.getTypeFactory().constructParametricType(BaseHttpRestResult.class, clazz);
    }

    @Override
    public Type getType() {
        return type;
    }
}
