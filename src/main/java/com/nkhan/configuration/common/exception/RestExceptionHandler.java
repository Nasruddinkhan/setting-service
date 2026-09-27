package com.nkhan.configuration.common.exception;


import com.nkhan.configuration.common.enums.ErrorTypeEnum;
import com.nkhan.configuration.common.model.ResponseHeader;
import com.nkhan.configuration.common.model.ResponseMessage;
import com.nkhan.configuration.common.model.ResponseStatus;
import com.nkhan.configuration.common.model.SubError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static com.nkhan.configuration.common.CommonConstants.CORRELATION_ID_HEADER;

/**
 * Global exception handler for REST API.
 * Maps all exceptions to standardized ResponseMessage format.
 * 
 * @author ngkhan
 */
@RestControllerAdvice
public class RestExceptionHandler {


    /**
     * Handles validation errors from @Valid annotations.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage<Void>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        List<SubError> subErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new SubError(error.getField(), error.getDefaultMessage()))
                .toList();

        return buildErrorResponse("E000002", "Validation failed", subErrors, 
                HttpStatus.BAD_REQUEST, getRequestId(request));
    }



    /**
     * Handles illegal argument exceptions.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessage<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        return buildErrorResponse("E000003", ex.getMessage(), null, 
                HttpStatus.BAD_REQUEST, getRequestId(request));
    }

    /**
     * Handles illegal argument exceptions.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ResponseMessage<Void>> handleDuplicateRecord(
            DuplicateResourceException ex, WebRequest request) {

        return buildErrorResponse("E000005", ex.getMessage(), null,
                HttpStatus.CONFLICT, getRequestId(request));
    }

    /**
     * Handles resource not found exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseMessage<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        return buildErrorResponse("E000004", ex.getMessage(), null, 
                HttpStatus.NOT_FOUND, getRequestId(request));
    }

    /**
     * Handles all other unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage<Void>> handleGenericException(
            Exception ex, WebRequest request) {
        ex.printStackTrace();
        return buildErrorResponse("E999999", "Internal server error", null,
                HttpStatus.INTERNAL_SERVER_ERROR, getRequestId(request));
    }




    /**
     * Builds a standardized error response.
     */
    private ResponseEntity<ResponseMessage<Void>> buildErrorResponse(
            String code, String details, List<SubError> subErrors,
            HttpStatus httpStatus, String requestId) {

        ResponseStatus status = new ResponseStatus();
        status.setCode(code);
        status.setType(ErrorTypeEnum.ERROR);
        status.setTitle(details);
        status.setDetails(details);
        status.setHttpStatusCode(httpStatus);
        if (subErrors != null && !subErrors.isEmpty()) {
            status.setSubErrors(subErrors);
        }

        ResponseHeader header = new ResponseHeader();
        header.setStatus(status);
        header.setRequestId(requestId);
        ResponseMessage<Void> response = new ResponseMessage<>();
        response.setHeader(header);
        response.setSuccessfulResponse(false);
        return new ResponseEntity<>(response, httpStatus);
    }


    /**
     * Extracts request ID from headers.
     */
    private String getRequestId(WebRequest request) {
        String requestId = request.getHeader(CORRELATION_ID_HEADER);
        return requestId != null ? requestId : "unknown";
    }
}

