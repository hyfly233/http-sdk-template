package com.hyfly.http.sdk.remote.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyfly.http.sdk.remote.BaseHttpRestResult;
import com.hyfly.http.sdk.remote.TypeReferenceHttpResult;
import com.hyfly.http.sdk.remote.response.HttpClientResponse;
import org.apache.http.HttpStatus;

import java.io.InputStream;

public class ResponseHandler<T> {

    private Class<T> responseType;

    public final void setResponseType(Class<T> responseType) {
        this.responseType = responseType;
    }

    public final BaseHttpRestResult<T> handle(HttpClientResponse response) throws Exception {

        if (HttpStatus.SC_BAD_REQUEST < response.getStatusCode()) {
            return handleError(response);
        }
        return convertResult(response, this.responseType);
    }

    public final BaseHttpRestResult<T> handleError(HttpClientResponse response) throws Exception {

        return null;
    }

    public BaseHttpRestResult<T> convertResult(HttpClientResponse response, Class<T> responseType)
            throws Exception {
        InputStream is = response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(is, new TypeReferenceHttpResult<>(responseType));
    }
}
