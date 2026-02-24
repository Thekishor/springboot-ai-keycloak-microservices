package com.ai_service.controller;

import com.ai_service.dto.RecommendationResponse;
import com.ai_service.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getUserRecommendation(@PathVariable("userId") String userId) {
        List<RecommendationResponse> recommendationResponses = recommendationService.getUserRecommendation(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(recommendationResponses);
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<RecommendationResponse> getActivityRecommendation(@PathVariable("activityId") String activityId) {
        RecommendationResponse recommendationResponses = recommendationService.getActivityRecommendation(activityId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(recommendationResponses);
    }
}
