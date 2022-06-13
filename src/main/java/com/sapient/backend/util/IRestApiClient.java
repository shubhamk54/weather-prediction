package com.sapient.backend.util;

import com.sapient.backend.exception.ApiException;

public interface IRestApiClient {

    IRestApiClient addHeader(String headerName, String headerValue);

    IRestApiClient queryParam(String key, String value);

    <T> T build(Class<T> clazz) throws ApiException;

    IRestApiClient body(String field, String value);

}
