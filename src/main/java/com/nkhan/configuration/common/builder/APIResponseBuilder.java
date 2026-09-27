package com.nkhan.configuration.common.builder;


import com.nkhan.configuration.common.model.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class APIResponseBuilder<T, E> {

    @Autowired
    protected AppResponseBuilder responseBuilder;


    public ResponseMessage<E> buildServiceOutput(T serviceResponse, String requestId) {
        ResponseMessage<E> resp = responseBuilder.buildResponse(serviceResponse, requestId);
        if (serviceResponse != null) {
            resp.setData(transformMessage(serviceResponse));
        }
        resp.setSuccessfulResponse(true);
        return resp;
    }


    protected abstract E transformMessage(T serviceResponse);


    public ResponseMessage<Void> buildDeleteOutput(String requestId) {
        ResponseMessage<Void> resp = responseBuilder.buildResponse(null, requestId);
        resp.setSuccessfulResponse(true);
        return resp;
    }

}
