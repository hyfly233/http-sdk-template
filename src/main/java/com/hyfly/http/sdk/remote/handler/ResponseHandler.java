package com.hyfly.http.sdk.remote.handler;

public class ResponseHandler<T> {

    private Class<T> responseType;

    public final void setResponseType(Class<T> responseType) {
        this.responseType = responseType;
    }

}
