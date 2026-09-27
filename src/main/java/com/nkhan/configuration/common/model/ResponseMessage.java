package com.nkhan.configuration.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * Generic wrapper for API responses.
 * Contains a header with status information and a data payload.
 * 
 * @param <T> the type of the response data
 * @author ngkhan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"header", "data", "successfulResponse"})
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseMessage<T> {

    /** Response header containing status and request ID */
    @JsonProperty("header")
    private ResponseHeader header;

    /** The response payload */
    @JsonProperty("body")
    private T data;
    private Boolean successfulResponse;
}

