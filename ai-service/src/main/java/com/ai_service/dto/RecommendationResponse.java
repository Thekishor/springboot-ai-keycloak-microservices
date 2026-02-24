package com.ai_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationResponse {

    private String id;
    private String activityId;
    private String userId;
    private String activityType;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;
}
