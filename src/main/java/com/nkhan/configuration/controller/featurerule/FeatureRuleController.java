package com.nkhan.configuration.controller.featurerule;

import com.nkhan.configuration.common.builder.GenericResponseBuilder;
import com.nkhan.configuration.common.model.ResponseMessage;
import com.nkhan.configuration.mapper.FeatureRuleMapper;
import com.nkhan.configuration.service.FeatureRuleService;
import com.nkhan.configuration.controller.featurerule.vo.request.FeatureRuleReq;
import com.nkhan.configuration.controller.featurerule.vo.response.FeatureRuleRespVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.context.path}/feature-rules")
public class FeatureRuleController {

    private final FeatureRuleService featureRuleService;
    private final FeatureRuleMapper featureRuleMapper;
    private final GenericResponseBuilder responseBuilder;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<FeatureRuleRespVO>>> findAllFeatureRules(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildListResponse(
                featureRuleService.findAllFeatureRules(),
                featureRuleMapper::toRespVOs,
                requestId));
    }

    @GetMapping("/{ruleId}")
    public ResponseEntity<ResponseMessage<FeatureRuleRespVO>> findFeatureRuleById(
            @PathVariable @NotNull @Positive Long ruleId,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                featureRuleService.findFeatureRuleById(ruleId),
                featureRuleMapper::toRespVO,
                requestId));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<FeatureRuleRespVO>> createFeatureRule(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody FeatureRuleReq req) {
        var serviceReq = featureRuleMapper.toEntity(req);
        var serviceRes = featureRuleService.createFeatureRule(serviceReq, req.getFeatureId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.buildSingleResponse(
                serviceRes,
                featureRuleMapper::toRespVO,
                requestId));
    }

    @PutMapping("/{ruleId}")
    public ResponseEntity<ResponseMessage<FeatureRuleRespVO>> updateFeatureRule(
            @PathVariable @NotNull @Positive Long ruleId,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody FeatureRuleReq req) {
        var serviceReq = featureRuleMapper.toEntity(req);
        var serviceRes = featureRuleService.updateFeatureRule(ruleId, serviceReq, req.getFeatureId());
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                serviceRes,
                featureRuleMapper::toRespVO,
                requestId));
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteFeatureRule(@PathVariable @NotNull @Positive Long ruleId) {
        featureRuleService.deleteFeatureRule(ruleId);
        return ResponseEntity.noContent().build();
    }
}
