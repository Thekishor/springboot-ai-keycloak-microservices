package com.activity_service.service;

import com.activity_service.dto.ActivityRequest;
import com.activity_service.dto.ActivityResponse;

import java.util.List;

public interface ActivityService {

    ActivityResponse createActivity(ActivityRequest activityRequest);

    List<ActivityResponse> getUsersActivity(String userId);

    ActivityResponse getActivityById(String activityId);
}
