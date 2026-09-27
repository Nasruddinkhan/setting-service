package com.nkhan.configuration.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the header section of an API response.
 * Contains the request ID for traceability and the response status.
 * 
 * @author ngkhan
 */
@JsonPropertyOrder({"requestReceivedAt", "responseSentAt", "requestId", "status"})
@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ResponseHeader {

    /** The status of the response */
    @JsonProperty("status")
    private ResponseStatus status;

    /** The unique request identifier for tracing */
    @JsonProperty("rqUID")
    private String requestId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestReceivedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime responseSentAt;

    private List<ResponseMeta> meta;
}

