package com.ai_service.service;

import com.ai_service.document.Recommendation;
import com.ai_service.dto.RecommendationResponse;
import com.ai_service.exception.ActivityNotFoundException;
import com.ai_service.exception.UserNotFoundException;
import com.ai_service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Override
    public List<RecommendationResponse> getUserRecommendation(String userId) {
        List<Recommendation> recommendations = recommendationRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id"));
        return recommendations.stream().map(this::mapRecommendationEntityToRecommendationResponse).toList();
    }

    @Override
    public RecommendationResponse getActivityRecommendation(String activityId) {
        Recommendation recommendation = recommendationRepository.findByActivityId(activityId)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found with id"));
        return mapRecommendationEntityToRecommendationResponse(recommendation);
    }

    private RecommendationResponse mapRecommendationEntityToRecommendationResponse(Recommendation recommendation) {
        return RecommendationResponse.builder()
                .id(recommendation.getId())
                .userId(recommendation.getUserId())
                .activityId(recommendation.getActivityId())
                .recommendation(recommendation.getRecommendation())
                .activityType(recommendation.getActivityType())
                .improvements(recommendation.getImprovements())
                .suggestions(recommendation.getSuggestions())
                .safety(recommendation.getSafety())
                .build();
    }
}
