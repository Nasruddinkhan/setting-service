package com.nkhan.configuration.common.builder;

import com.nkhan.configuration.common.model.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class GenericResponseBuilder {

    private final AppResponseBuilder responseBuilder;

    public <S, R> ResponseMessage<List<R>> buildListResponse(
            List<S> serviceResult,
            Function<List<S>, List<R>> transformer,
            String rquid) {
        List<R> body = (serviceResult == null || serviceResult.isEmpty())
                ? List.of()
                : transformer.apply(serviceResult);
        ResponseMessage<List<R>> response = responseBuilder.buildResponse(body, rquid);
        response.setData(body);
        response.setSuccessfulResponse(true);
        return response;

    }

    public <S, R> ResponseMessage<R> buildSingleResponse(
            S serviceResult,
            Function<S, R> transformer,
            String rquid) {
        var body = serviceResult == null ? null : transformer.apply(serviceResult);
        ResponseMessage<R> response = responseBuilder.buildResponse(body, rquid);
        response.setData(body);
        response.setSuccessfulResponse(true);
        return response;

    }

}
