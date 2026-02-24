package com.ai_service.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class Activity {

    private String id;
    private String userId;
    private String type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
