package com.sapient.backend.util;

import com.sapient.backend.exception.ApiException;
import com.sapient.backend.exception.DataNotFoundException;
import com.sapient.backend.exception.InternalServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

public class RestApiClient implements IRestApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(RestApiClient.class);
    private final String url;
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    HttpMethod httpMethod;
    HttpHeaders headers = new HttpHeaders();
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();


    public RestApiClient(HttpMethod httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public RestApiClient(String url) {
        this.httpMethod = HttpMethod.GET;
        this.url = url;
    }

    public String getUrlTemplate() {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(queryParams)
                .encode()
                .toUriString();
    }

    @Override
    public IRestApiClient queryParam(String key, String value) {
        queryParams.put(key, Collections.singletonList(value));
        return this;
    }

    @Override
    public <T> T build(Class<T> clazz) throws ApiException {

        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        try {
            LOG.debug("HTTP Method: {} URL: {}", this.httpMethod, url);

            ResponseEntity<T> response = restTemplate.exchange(getUrlTemplate(), httpMethod, entity, clazz);
            LOG.info("HTTP Status code: {} received.", response.getStatusCodeValue());
            return response.getBody();
        } catch (HttpClientErrorException exception) {

            switch (exception.getStatusCode()) {
                case NOT_FOUND:
                    LOG.error("Resource not available: {}", exception.getMessage());
                    throw new DataNotFoundException("Resource not available");
                case UNAUTHORIZED:
                    LOG.error("Unable to connect due to access issue: {}", exception.getMessage());
                    throw new InternalServerException(exception.getMessage());
                default:
                    LOG.error("REST client Exception:{}", exception.getMessage());
                    throw new InternalServerException(exception.getMessage());
            }
        }
    }

    @Override
    public IRestApiClient body(String field, String value) {
        body.add(field, value);
        return this;
    }

    @Override
    public IRestApiClient addHeader(String headerName, String headerValue) {
        headers.set(headerName, headerValue);
        return this;
    }
}
