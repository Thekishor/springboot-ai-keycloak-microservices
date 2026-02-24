package com.ai_service.service;

import com.ai_service.dto.RecommendationResponse;

import java.util.List;

public interface RecommendationService {
    List<RecommendationResponse> getUserRecommendation(String userId);

    RecommendationResponse getActivityRecommendation(String activityId);
}
