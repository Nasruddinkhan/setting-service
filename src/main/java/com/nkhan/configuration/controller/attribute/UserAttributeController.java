package com.nkhan.configuration.controller.attribute;

import com.nkhan.configuration.service.UserAttributeService;
import com.nkhan.configuration.controller.attribute.vo.request.UserAttributeReq;
import com.nkhan.configuration.controller.attribute.vo.response.UserAttributeRespVO;
import com.nkhan.configuration.common.builder.GenericResponseBuilder;
import com.nkhan.configuration.common.model.ResponseMessage;
import com.nkhan.configuration.mapper.UserAttributeMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nkhan.configuration.common.CommonConstants.CORRELATION_ID_HEADER;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.context.path}/user-attributes")
public class UserAttributeController {

    private final UserAttributeService userAttributeService;
    private final UserAttributeMapper userAttributeMapper;
    private final GenericResponseBuilder responseBuilder;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<UserAttributeRespVO>>> findAllUserAttributes(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildListResponse(
                userAttributeService.findAllUserAttributes(),
                userAttributeMapper::toRespVOs,
                requestId));
    }

    @GetMapping("/{attributeKey}")
    public ResponseEntity<ResponseMessage<UserAttributeRespVO>> findUserAttributeById(
            @PathVariable @NotBlank String attributeKey,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                userAttributeService.findUserAttributeById(attributeKey),
                userAttributeMapper::toRespVO,
                requestId));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<UserAttributeRespVO>> createUserAttribute(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody UserAttributeReq req) {
        var serviceReq = userAttributeMapper.toEntity(req);
        var serviceRes = userAttributeService.createUserAttribute(serviceReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.buildSingleResponse(
                serviceRes,
                userAttributeMapper::toRespVO,
                requestId));
    }

    @PutMapping("/{attributeKey}")
    public ResponseEntity<ResponseMessage<UserAttributeRespVO>> updateUserAttribute(
            @PathVariable @NotBlank String attributeKey,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody UserAttributeReq req) {
        var serviceReq = userAttributeMapper.toEntity(req);
        var serviceRes = userAttributeService.updateUserAttribute(attributeKey, serviceReq);
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                serviceRes,
                userAttributeMapper::toRespVO,
                requestId));
    }

    @DeleteMapping("/{attributeKey}")
    public ResponseEntity<Void> deleteUserAttribute(@PathVariable @NotBlank String attributeKey) {
        userAttributeService.deleteUserAttribute(attributeKey);
        return ResponseEntity.noContent().build();
    }
}
