package com.nkhan.configuration.common.model;

import com.nkhan.configuration.common.enums.ErrorTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Represents the status of an API response.
 * Contains status code, details, and optional sub-errors for validation.
 * 
 * @author ngkhan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code","type","title", "details", "subErrors"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStatus {
    /** Business status code (e.g., S000000 for success, E000001 for error) */
    @JsonProperty("code")
    private String code;

    /** SUCCESS / ERROR */
    @JsonProperty("type")
    private ErrorTypeEnum type;

    /** Short UI-friendly message */
    @JsonProperty("title")
    private String title;

    /** Detailed description of the status */
    @JsonProperty("desc")
    private String details;

    /** List of validation / business errors */
    @JsonProperty("errors")
    private List<SubError> subErrors;

    /** HTTP status code (internal use only, not exposed in API response) */
    @JsonIgnore
    private HttpStatus httpStatusCode;

}

