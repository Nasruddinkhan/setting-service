package com.nkhan.configuration.common.builder;


import com.nkhan.configuration.common.enums.ErrorTypeEnum;
import com.nkhan.configuration.common.model.ResponseHeader;
import com.nkhan.configuration.common.model.ResponseMessage;
import com.nkhan.configuration.common.model.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class AppResponseBuilder {

    private static final String SUCCESS_CODE = "S000000";

    public <T> ResponseMessage<T> buildResponse(Object erpResponse, String requestId) {
        ResponseMessage<T> response = new ResponseMessage<>();
        ResponseHeader header = new ResponseHeader();
        ResponseStatus status = buildStatus(erpResponse);
        response.setSuccessfulResponse(true);
        header.setStatus(status);
        header.setRequestId(requestId);
        response.setHeader(header);
        return response;
    }


    private ResponseStatus buildStatus(Object source) {
        ResponseStatus status = new ResponseStatus();
        status.setCode(SUCCESS_CODE);
        status.setType(source != null ? ErrorTypeEnum.SUCCESS : ErrorTypeEnum.WARNING);
        status.setDetails("Request processed successfully");
        status.setHttpStatusCode(HttpStatus.OK);
        return status;
    }

    /**
     * Checks if the return code indicates success.
     */




}
