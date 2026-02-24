package com.activity_service.document;

import com.activity_service.enums.ActivityType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "activities")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Activity {

    @Id
    private String id;

    private String userId;

    private ActivityType activityType;

    private Integer duration;

    private Integer caloriesBurned;

    private LocalDateTime startTime;

    @Field("metrics")
    private Map<String, Object> additionalMetrics;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
