package com.ai_service.service;

import com.ai_service.document.Recommendation;
import com.ai_service.dto.Activity;
import com.ai_service.dto.AiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final ChatClient chatClient;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);

        AiResponse response = chatClient
                .prompt()
                .system("You are a professional fitness AI. Return ONLY valid JSON. No markdown.")
                .user(prompt)
                .call()
                .entity(AiResponse.class);
        log.info("Response from AI {}", response);
        return buildRecommendation(activity, response);
    }

    private Recommendation buildRecommendation(Activity activity, AiResponse response) {
        if (response == null) {
            return createDefaultRecommendation(activity);
        }

        String fullAnalysis = """
                Overall: %s
                Pace: %s
                Heart: %s
                Calories: %s
                """.formatted(
                response.getAnalysis().getOverall(),
                response.getAnalysis().getPace(),
                response.getAnalysis().getHeartRate(),
                response.getAnalysis().getCaloriesBurned()
        );

        return Recommendation.builder()
                .activityType(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation(fullAnalysis)
                .improvements(
                        response.getImprovements().stream()
                                .map(i -> i.getArea() + ":" + i.getRecommendation())
                                .toList()
                )
                .suggestions(
                        response.getSuggestions().stream()
                                .map(s -> s.getWorkout() + ":" + s.getDescription())
                                .toList()
                )
                .safety(response.getSafety())
                .build();
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to generate detailed analysis")
                .improvements(List.of("Continue with your current routine"))
                .suggestions(List.of("Consider consulting a fitness professional"))
                .safety(List.of(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
                                {
                                  "analysis": {
                                    "overall": "Overall analysis here",
                                    "pace": "Pace analysis here",
                                    "heartRate": "Heart rate analysis here",
                                    "caloriesBurned": "Calories analysis here"
                                  },
                                  "improvements": [
                                    {
                                      "area": "Area name",
                                      "recommendation": "Detailed recommendation"
                                    }
                                  ],
                                  "suggestions": [
                                    {
                                      "workout": "Workout name",
                                      "description": "Detailed workout description"
                                    }
                                  ],
                                  "safety": [
                                    "Safety point 1",
                                    "Safety point 2"
                                  ]
                                }
                        Analyze this fitness activity:
                        Activity Type: %s
                        Duration: %d minutes
                        Calories Burned: %d
                        Additional Metrics: %s
                        
                        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
                        Ensure the response follows the EXACT JSON format shown above.
                        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}