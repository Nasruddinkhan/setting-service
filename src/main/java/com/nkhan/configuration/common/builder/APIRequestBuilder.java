package com.nkhan.configuration.common.builder;

/**
 * Abstract base class for building a service request from an API request.
 * It handles client requests and transforms them into service/ERP requests.
 *
 * @param <R> the request type from the controller/API layer (input)
 * @param <T> the transformed service/ERP request type (input)
 *
 * @author ngkhan
 */
public abstract class APIRequestBuilder<R, T> {

    /**
     * Builds the final service/ERP request from an API request.
     *
     * @param apiRequest the request coming from the controller/API layer
     * @return the constructed service/ERP request object
     */
    public T buildServiceInput(R apiRequest) {
        return transformRequest(apiRequest);
    }

    /**
     * Transforms the API request into the service/ERP request format.
     * Implementations should map API request fields to the ERP/service DTO.
     *
     * @param apiRequest the API request
     * @return the transformed service/ERP request
     */
    protected abstract T transformRequest(R apiRequest);
}
