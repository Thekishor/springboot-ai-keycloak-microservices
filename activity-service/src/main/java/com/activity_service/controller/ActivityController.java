package com.activity_service.controller;

import com.activity_service.dto.ActivityRequest;
import com.activity_service.dto.ActivityResponse;
import com.activity_service.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(@RequestBody ActivityRequest activityRequest) {
        ActivityResponse activity = activityService.createActivity(activityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(activity);
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getAllUserActivity(@RequestHeader("X-User-Id") String userId) {
        List<ActivityResponse> activityResponses = activityService.getUsersActivity(userId);
        return ResponseEntity.status(HttpStatus.OK).body(activityResponses);
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable("activityId") String activityId) {
        ActivityResponse activityResponse = activityService.getActivityById(activityId);
        return ResponseEntity.status(HttpStatus.OK).body(activityResponse);
    }
}
