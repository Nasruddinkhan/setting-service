package com.nkhan.configuration.controller.appication;

import com.nkhan.configuration.common.builder.GenericResponseBuilder;
import com.nkhan.configuration.common.model.ResponseMessage;
import com.nkhan.configuration.controller.appication.builder.requests.ApplicationConfigCreateReqBuilder;
import com.nkhan.configuration.controller.appication.vo.requests.ApplicationConfigCreateReq;
import com.nkhan.configuration.controller.appication.vo.responses.ApplicationConfigRespVO;
import com.nkhan.configuration.mapper.ApplicationConfigMapper;
import com.nkhan.configuration.service.ApplicationConfigService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.nkhan.configuration.common.CommonConstants.CORRELATION_ID_HEADER;

@RestController
@Slf4j
@RequestMapping("${api.context.path}/application-cfg")
@RequiredArgsConstructor
public class ApplicationConfigController {

    private final ApplicationConfigService applicationConfigService;
    private final ApplicationConfigCreateReqBuilder configCreateReqBuilder;
    private final GenericResponseBuilder genericResponseBuilder;
    private final ApplicationConfigMapper configMapper;
    /***
     * @param requestId
     * @return
     */
    @GetMapping
    public ResponseEntity<ResponseMessage<List<ApplicationConfigRespVO>>> findAllConfiguration(@RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(genericResponseBuilder.buildListResponse(
                applicationConfigService.findAllConfiguration(),
                configMapper::toApplicationConfigRespVOs,
                requestId));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<ApplicationConfigRespVO>> createConfiguration(@RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
                                                                                        @Valid @RequestBody ApplicationConfigCreateReq req) {
        final var reqBO = configCreateReqBuilder.buildServiceInput(req);
        final var res = genericResponseBuilder.buildSingleResponse(
                applicationConfigService.createConfiguration(reqBO),
                configMapper::toApplicationConfigRespVO,
                requestId);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/{application-Id}")
    public ResponseEntity<ResponseMessage<ApplicationConfigRespVO>> updateConfiguration(
            @PathVariable(name = "application-Id") @NotNull @Positive Long id,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody ApplicationConfigCreateReq req) {

        final var reqBO = configCreateReqBuilder.buildServiceInput(req);
        final var res = genericResponseBuilder.buildSingleResponse(
                applicationConfigService.updateConfiguration(id, reqBO),
                configMapper::toApplicationConfigRespVO,
                requestId);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{application-Id}")
    public ResponseEntity<Void> deleteConfiguration(
            @PathVariable(name = "application-Id") @NotNull @Positive Long id,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @RequestHeader(value = "x-updated-by", required = false) String updatedBy) {
        applicationConfigService.deleteConfiguration(id, updatedBy);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{application-Id}/permanent")
    public ResponseEntity<ResponseMessage<Void>> deleteConfigurationPermanently(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @PathVariable(name = "application-Id") @NotNull @Positive Long id) {
        applicationConfigService.deleteConfigurationPermanently(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{type}/feature-type")
    public ResponseEntity<ResponseMessage<List<ApplicationConfigRespVO>>> findAllConfigurationByType(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @PathVariable(name = "type") @NotNull  String featureType) {
        return ResponseEntity.ok(genericResponseBuilder.buildListResponse(
                applicationConfigService.findAllConfigurationByType(featureType),
                configMapper::toApplicationConfigRespVOs,
                requestId));
    }
    @GetMapping("/features/{type}/{code}")
    public ResponseEntity<ResponseMessage<ApplicationConfigRespVO>> findAllConfigurationByType(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @PathVariable(name = "code") @NotNull  String code,
            @PathVariable(name = "type") @NotNull  String featureType) {
        return ResponseEntity.ok(genericResponseBuilder.buildSingleResponse(
                applicationConfigService.getFeatureByCode(code, featureType).orElse(null),
                configMapper::toApplicationConfigRespVO,
                requestId));
    }
}
