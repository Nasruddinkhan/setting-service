package com.nkhan.configuration.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a sub-error in the API response.
 * Used to provide detailed field-level error information.
 * 
 * @author ngkhan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubError {

    /** The field that caused the error */
    @JsonProperty("code")
    private String field;

    /** Detailed description of the error */
    @JsonProperty("details")
    private Object details;

}

