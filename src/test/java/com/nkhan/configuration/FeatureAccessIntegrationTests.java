package com.nkhan.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nkhan.configuration.common.enums.AttributeSourceType;
import com.nkhan.configuration.model.FeatureEntity;
import com.nkhan.configuration.model.FeatureRuleEntity;
import com.nkhan.configuration.model.RuleConditionEntity;
import com.nkhan.configuration.model.UserAttributeEntity;
import com.nkhan.configuration.repository.FeatureRepo;
import com.nkhan.configuration.repository.FeatureRuleRepo;
import com.nkhan.configuration.repository.RuleConditionRepo;
import com.nkhan.configuration.repository.UserAttributeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FeatureAccessIntegrationTests {

    private static final String REQUEST_ID_HEADER = "x-nkhan-client-version";
    private static final String REQUEST_ID = "integration-test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FeatureRepo featureRepo;

    @Autowired
    private FeatureRuleRepo featureRuleRepo;

    @Autowired
    private RuleConditionRepo ruleConditionRepo;

    @Autowired
    private UserAttributeRepo userAttributeRepo;

    private SeedData seedData;

    @BeforeEach
    void setUp() {
        ruleConditionRepo.deleteAllInBatch();
        featureRuleRepo.deleteAllInBatch();
        userAttributeRepo.deleteAllInBatch();
        featureRepo.deleteAllInBatch();

        seedData = seedFeatureAccessData();
    }

    @Test
    void accessEndpointReturnsEveryFeatureAndRuleAsTrueFalseForUsername() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/features/access")
                        .param("username", "ngkhan")
                        .header(REQUEST_ID_HEADER, REQUEST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.rqUID").value(REQUEST_ID))
                .andExpect(jsonPath("$.header.status.code").value("S000000"))
                .andExpect(jsonPath("$.successfulResponse").value(true))
                .andReturn();

        JsonNode body = responseBody(result);

        assertThat(body.get("LOGIN").asBoolean()).isTrue();
        assertThat(body.get("NOTIFICATIONS").asBoolean()).isTrue();
        assertThat(body.get("ADMIN_PANEL").asBoolean()).isFalse();
        assertThat(body.get("MPIN_LOGIN").asBoolean()).isFalse();
        assertThat(body.get("BIOMETRIC_LOGIN").asBoolean()).isTrue();
        assertThat(body.get("PUSH_NOTIFICATION").asBoolean()).isTrue();
    }

    @Test
    void blockedEndpointsReturnRuleBlocksForUsernameAndGlobalBlocksForEveryone() throws Exception {
        MvcResult userBlockedResult = mockMvc.perform(get("/api/v1/features/blocked")
                        .param("username", "ngkhan")
                        .header(REQUEST_ID_HEADER, REQUEST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successfulResponse").value(true))
                .andReturn();

        JsonNode userBlockedFeatures = responseBody(userBlockedResult);
        assertThat(userBlockedFeatures).hasSize(2);
        assertThat(findByFeatureId(userBlockedFeatures, "ADMIN_PANEL").get("blockedBy").asText()).isEqualTo("GLOBAL");
        JsonNode loginBlock = findByFeatureId(userBlockedFeatures, "LOGIN");
        assertThat(loginBlock.get("blockedBy").asText()).isEqualTo("RULE");
        assertThat(loginBlock.get("ruleCode").asText()).isEqualTo("MPIN_LOGIN");
        assertThat(loginBlock.get("ruleId").asLong()).isEqualTo(seedData.mpinRule().getRuleId());

        MvcResult globalBlockedResult = mockMvc.perform(get("/api/v1/features/blocked/global")
                        .header(REQUEST_ID_HEADER, REQUEST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successfulResponse").value(true))
                .andReturn();

        JsonNode globallyBlockedFeatures = responseBody(globalBlockedResult);
        assertThat(globallyBlockedFeatures).hasSize(1);
        JsonNode adminPanelBlock = globallyBlockedFeatures.get(0);
        assertThat(adminPanelBlock.get("featureId").asText()).isEqualTo("ADMIN_PANEL");
        assertThat(adminPanelBlock.get("blockedBy").asText()).isEqualTo("GLOBAL");
        assertThat(adminPanelBlock.get("blockedGlobally").asBoolean()).isTrue();
    }

    @Test
    void accessGraphEndpointGroupsRulesUnderTheirParentFeature() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/features/access/graph")
                        .param("username", "ngkhan")
                        .header(REQUEST_ID_HEADER, REQUEST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successfulResponse").value(true))
                .andReturn();

        JsonNode features = responseBody(result).get("features");

        JsonNode login = findByFeatureId(features, "LOGIN");
        assertThat(login.get("blocked").asBoolean()).isFalse();
        assertThat(login.get("rules").get("MPIN_LOGIN").asBoolean()).isFalse();
        assertThat(login.get("rules").get("BIOMETRIC_LOGIN").asBoolean()).isTrue();

        JsonNode notifications = findByFeatureId(features, "NOTIFICATIONS");
        assertThat(notifications.get("blocked").asBoolean()).isFalse();
        assertThat(notifications.get("rules").get("PUSH_NOTIFICATION").asBoolean()).isTrue();

        JsonNode adminPanel = findByFeatureId(features, "ADMIN_PANEL");
        assertThat(adminPanel.get("blocked").asBoolean()).isTrue();
        assertThat(adminPanel.get("rules").isEmpty()).isTrue();
    }

    @Test
    void addConditionValuesEndpointMergesValuesAndPersistsJsonBackedSet() throws Exception {
        Long conditionId = seedData.mpinCondition().getConditionId();
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "values", List.of("fatima", "ngkhan")
        ));

        MvcResult result = mockMvc.perform(patch("/api/v1/rule-conditions/{conditionId}/values", conditionId)
                        .header(REQUEST_ID_HEADER, REQUEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successfulResponse").value(true))
                .andExpect(jsonPath("$.body.conditionId").value(conditionId))
                .andExpect(jsonPath("$.body.ruleId").value(seedData.mpinRule().getRuleId()))
                .andExpect(jsonPath("$.body.attributeKey").value("user_id"))
                .andReturn();

        JsonNode values = responseBody(result).get("attributeValue");
        assertThat(values).hasSize(2);
        assertThat(values).extracting(JsonNode::asText).containsExactlyInAnyOrder("ngkhan", "fatima");

        RuleConditionEntity reloadedCondition = ruleConditionRepo.findById(conditionId).orElseThrow();
        assertThat(reloadedCondition.getAttributeValue()).containsExactlyInAnyOrder("ngkhan", "fatima");
    }

    private SeedData seedFeatureAccessData() {
        FeatureEntity login = saveFeature("LOGIN", "Login", false);
        FeatureEntity notifications = saveFeature("NOTIFICATIONS", "Notifications", false);
        saveFeature("ADMIN_PANEL", "Admin Panel", true);

        UserAttributeEntity userIdAttribute = new UserAttributeEntity();
        userIdAttribute.setAttributeKey("user_id");
        userIdAttribute.setSourceType(AttributeSourceType.CLAIM);
        userIdAttribute.setPriority(1);
        userIdAttribute.setCreatedBy("TEST");
        userIdAttribute.setIsActive(true);
        userAttributeRepo.saveAndFlush(userIdAttribute);

        FeatureRuleEntity mpinRule = saveRule(login, "MPIN_LOGIN", true);
        FeatureRuleEntity biometricRule = saveRule(login, "BIOMETRIC_LOGIN", true);
        FeatureRuleEntity pushRule = saveRule(notifications, "PUSH_NOTIFICATION", true);

        RuleConditionEntity mpinCondition = saveCondition(mpinRule, userIdAttribute, Set.of("ngkhan"));
        saveCondition(biometricRule, userIdAttribute, Set.of("other-user"));
        saveCondition(pushRule, userIdAttribute, Set.of("someone-else"));

        return new SeedData(mpinRule, mpinCondition);
    }

    private FeatureEntity saveFeature(String featureId, String displayName, boolean blockedGlobally) {
        FeatureEntity feature = new FeatureEntity();
        feature.setFeatureId(featureId);
        feature.setDisplayName(displayName);
        feature.setDescription(displayName + " feature");
        feature.setBlockedGlobally(blockedGlobally);
        feature.setMsgEn(displayName + " is unavailable");
        feature.setMsgAr(displayName + " is unavailable");
        feature.setCreatedBy("TEST");
        feature.setIsActive(true);
        return featureRepo.saveAndFlush(feature);
    }

    private FeatureRuleEntity saveRule(FeatureEntity feature, String ruleCode, boolean blocked) {
        FeatureRuleEntity rule = new FeatureRuleEntity();
        rule.setFeature(feature);
        rule.setRuleCode(ruleCode);
        rule.setBlocked(blocked);
        rule.setStartDate(LocalDate.now().minusDays(1));
        rule.setEndDate(LocalDate.now().plusDays(1));
        rule.setDenyMsgEn(ruleCode + " denied");
        rule.setDenyMsgAr(ruleCode + " denied");
        rule.setCreatedBy("TEST");
        rule.setIsActive(true);
        return featureRuleRepo.saveAndFlush(rule);
    }

    private RuleConditionEntity saveCondition(FeatureRuleEntity rule, UserAttributeEntity attribute, Set<String> values) {
        RuleConditionEntity condition = new RuleConditionEntity();
        condition.setRule(rule);
        condition.setAttribute(attribute);
        condition.setAttributeValue(new LinkedHashSet<>(values));
        condition.setCreatedBy("TEST");
        condition.setIsActive(true);
        return ruleConditionRepo.saveAndFlush(condition);
    }

    private JsonNode responseBody(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("body");
    }

    private JsonNode findByFeatureId(JsonNode features, String featureId) {
        for (JsonNode feature : features) {
            if (featureId.equals(feature.get("featureId").asText())) {
                return feature;
            }
        }
        throw new AssertionError("Feature not found in response: " + featureId);
    }

    private record SeedData(FeatureRuleEntity mpinRule, RuleConditionEntity mpinCondition) {
    }
}
