package com.activity_service.service;

import com.activity_service.document.Activity;
import com.activity_service.dto.ActivityRequest;
import com.activity_service.dto.ActivityResponse;
import com.activity_service.exception.ActivityNotFoundException;
import com.activity_service.exception.UserNotFoundException;
import com.activity_service.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidateService userValidateService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Override
    public ActivityResponse createActivity(ActivityRequest activityRequest) {
        Activity activity = mapActivityRequestToActivityEntity(activityRequest);
        Activity savedActivity = activityRepository.save(activity);

        log.info("Publish message into rabbitmq for processing:");
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        } catch (Exception exception) {
            log.error("Failed to publish activity to rabbitmq: {}", exception.getMessage());
        }
        return mapActivityEntityToActivityResponse(savedActivity);
    }

    @Override
    public List<ActivityResponse> getUsersActivity(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream()
                .map(this::mapActivityEntityToActivityResponse)
                .toList();
    }

    @Override
    public ActivityResponse getActivityById(String activityId) {
        return activityRepository.findById(activityId)
                .map(this::mapActivityEntityToActivityResponse)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found"));
    }

    private ActivityResponse mapActivityEntityToActivityResponse(Activity savedActivity) {
        return ActivityResponse.builder()
                .id(savedActivity.getId())
                .userId(savedActivity.getUserId())
                .activityType(savedActivity.getActivityType())
                .duration(savedActivity.getDuration())
                .caloriesBurned(savedActivity.getCaloriesBurned())
                .startTime(savedActivity.getStartTime())
                .additionalMetrics(savedActivity.getAdditionalMetrics())
                .createdAt(savedActivity.getCreatedAt())
                .updatedAt(savedActivity.getUpdatedAt())
                .build();
    }

    private Activity mapActivityRequestToActivityEntity(ActivityRequest request) {
        boolean isValid = userValidateService.isValidUser(request.getUserId());
        if (!isValid) {
            throw new UserNotFoundException("User not found with Id: "+ request.getUserId());
        }
        return Activity.builder()
                .userId(request.getUserId())
                .activityType(request.getActivityType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
    }
}
