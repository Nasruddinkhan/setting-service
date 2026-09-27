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
public abstract class APIArgumentRequestBuilder<T> {

    /**
     * Builds the final service/ERP request from arguments only.
     */
    public final T buildServiceInput(Object... args) {
        return transformRequest(args);
    }

    /**
     * Transforms arguments into the service/ERP request format.
     */
    protected abstract T transformRequest(Object... args);
}
