package com.nkhan.configuration.service;


import com.nkhan.configuration.controller.feature.vo.responses.BlockedFeatureRespVO;
import com.nkhan.configuration.controller.feature.vo.responses.FeatureAccessGraphRespVO;
import com.nkhan.configuration.model.FeatureEntity;

import java.util.List;
import java.util.Map;

public interface FeatureService {
    List<FeatureEntity> findAllFeatures();

    FeatureEntity findFeatureById(String featureId);

    FeatureEntity createFeature(FeatureEntity req);

    FeatureEntity updateFeature(String featureId, FeatureEntity req);

    List<BlockedFeatureRespVO> findGloballyBlockedFeatures();

    List<BlockedFeatureRespVO> findBlockedFeaturesForUsername(String username);

    Map<String, Boolean> findFeatureAccessByUsername(String username);

    FeatureAccessGraphRespVO findFeatureAccessGraphByUsername(String username);

    void deleteFeature(String featureId);

}
