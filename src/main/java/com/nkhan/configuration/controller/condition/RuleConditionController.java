package com.nkhan.configuration.controller.condition;

import com.nkhan.configuration.common.builder.GenericResponseBuilder;
import com.nkhan.configuration.common.model.ResponseMessage;
import com.nkhan.configuration.mapper.RuleConditionMapper;
import com.nkhan.configuration.service.RuleConditionService;
import com.nkhan.configuration.controller.condition.vo.request.RuleConditionReq;
import com.nkhan.configuration.controller.condition.vo.request.RuleConditionValuesReq;
import com.nkhan.configuration.controller.condition.vo.response.RuleConditionRespVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("${api.context.path}/rule-conditions")
public class RuleConditionController {

    private final RuleConditionService ruleConditionService;
    private final RuleConditionMapper ruleConditionMapper;
    private final GenericResponseBuilder responseBuilder;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<RuleConditionRespVO>>> findAllRuleConditions(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildListResponse(
                ruleConditionService.findAllRuleConditions(),
                ruleConditionMapper::toRespVOs,
                requestId));
    }

    @GetMapping("/{conditionId}")
    public ResponseEntity<ResponseMessage<RuleConditionRespVO>> findRuleConditionById(
            @PathVariable @NotNull @Positive Long conditionId,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId) {
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                ruleConditionService.findRuleConditionById(conditionId),
                ruleConditionMapper::toRespVO,
                requestId));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<RuleConditionRespVO>> createRuleCondition(
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody RuleConditionReq req) {
        var serviceReq = ruleConditionMapper.toEntity(req);
        var serviceRes = ruleConditionService.createRuleCondition(
                serviceReq, req.getRuleId(), req.getAttributeKey());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.buildSingleResponse(
                serviceRes,
                ruleConditionMapper::toRespVO,
                requestId));
    }

    @PutMapping("/{conditionId}")
    public ResponseEntity<ResponseMessage<RuleConditionRespVO>> updateRuleCondition(
            @PathVariable @NotNull @Positive Long conditionId,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody RuleConditionReq req) {
        var serviceReq = ruleConditionMapper.toEntity(req);
        var serviceRes = ruleConditionService.updateRuleCondition(
                conditionId, serviceReq, req.getRuleId(), req.getAttributeKey());
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                serviceRes,
                ruleConditionMapper::toRespVO,
                requestId));
    }

    @PatchMapping("/{conditionId}/values")
    public ResponseEntity<ResponseMessage<RuleConditionRespVO>> addAttributeValues(
            @PathVariable @NotNull @Positive Long conditionId,
            @RequestHeader(value = CORRELATION_ID_HEADER, defaultValue = "default") String requestId,
            @Valid @RequestBody RuleConditionValuesReq req) {
        var serviceRes = ruleConditionService.addAttributeValues(conditionId, req.getValues());
        return ResponseEntity.ok(responseBuilder.buildSingleResponse(
                serviceRes,
                ruleConditionMapper::toRespVO,
                requestId));
    }

    @DeleteMapping("/{conditionId}")
    public ResponseEntity<Void> deleteRuleCondition(@PathVariable @NotNull @Positive Long conditionId) {
        ruleConditionService.deleteRuleCondition(conditionId);
        return ResponseEntity.noContent().build();
    }
}
