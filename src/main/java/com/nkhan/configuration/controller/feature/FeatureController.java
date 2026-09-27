package com.nkhan.configuration.controller.feature;

import com.nkhan.configuration.common.builder.GenericResponseBuilder;
import com.nkhan.configuration.common.model.ResponseMessage;
import com.nkhan.configuration.controller.feature.vo.requests.FeatureReq;
import com.nkhan.configuration.controller.feature.vo.responses.BlockedFeatureRespVO;
import com.nkhan.configuration.controller.feature.vo.responses.FeatureAccessGraphRespVO;
import com.nkhan.configuration.controller.feature.vo.responses.FeatureRespVO;
import com.nkhan.configuration.mapper.FeatureMapper;
import com.nkhan.configuration.service.FeatureService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.nkhan.configuration.common.CommonConstants.CORRELATION_ID_HEADER;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.context.path}/features")
public class FeatureController {

    private final FeatureService featureService;
    private final FeatureMapper featureMapper;
    private final GenericResponseBuilder responseBuilder;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<FeatureRespVO>>> findAllFeatures(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildListResponse(
                featureService.findAllFeatures(),
                featureMapper::toRespVOs,
                requestId));
    }

    @GetMapping("/blocked")
    public ResponseEntity<ResponseMessage<List<BlockedFeatureRespVO>>> findBlockedFeaturesForUsername(
            @RequestParam @NotBlank String username,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildListResponse(
                featureService.findBlockedFeaturesForUsername(username),
                blockedFeatures -> blockedFeatures,
                requestId));
    }

    @GetMapping("/blocked/global")
    public ResponseEntity<ResponseMessage<List<BlockedFeatureRespVO>>> findGloballyBlockedFeatures(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildListResponse(
                featureService.findGloballyBlockedFeatures(),
                blockedFeatures -> blockedFeatures,
                requestId));
    }

    @GetMapping("/access")
    public ResponseEntity<ResponseMessage<Map<String, Boolean>>> findFeatureAccessByUsername(
            @RequestParam @NotBlank String username,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                featureService.findFeatureAccessByUsername(username),
                featureAccess -> featureAccess,
                requestId));
    }

    @GetMapping("/access/graph")
    public ResponseEntity<ResponseMessage<FeatureAccessGraphRespVO>> findFeatureAccessGraphByUsername(
            @RequestParam @NotBlank String username,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                featureService.findFeatureAccessGraphByUsername(username),
                graph -> graph,
                requestId));
    }

    @GetMapping("/{featureId}")
    public ResponseEntity<ResponseMessage<FeatureRespVO>> findFeatureById(
            @PathVariable @NotBlank String featureId,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                featureService.findFeatureById(featureId),
                featureMapper::toRespVO,
                requestId));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<FeatureRespVO>> createFeature(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody FeatureReq req) {
        var serviceReq = featureMapper.toEntity(req);
        var serviceRes = featureService.createFeature(serviceReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.buildSingleResponse(
                serviceRes,
                featureMapper::toRespVO,
                requestId));
    }

    @PutMapping("/{featureId}")
    public ResponseEntity<ResponseMessage<FeatureRespVO>> updateFeature(
            @PathVariable @NotBlank String featureId,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody FeatureReq req) {
        var serviceReq = featureMapper.toEntity(req);
        var serviceRes = featureService.updateFeature(featureId, serviceReq);
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                serviceRes,
                featureMapper::toRespVO,
                requestId));
    }

    @DeleteMapping("/{featureId}")
    public ResponseEntity<Void> deleteFeature(@PathVariable @NotBlank String featureId) {
        featureService.deleteFeature(featureId);
        return ResponseEntity.noContent().build();
    }
}
