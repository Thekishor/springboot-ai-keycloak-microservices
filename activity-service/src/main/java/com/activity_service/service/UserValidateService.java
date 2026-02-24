package com.activity_service.service;

import com.activity_service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidateService {

    private final WebClient webClient;

    public boolean isValidUser(String userId) {
        try {
              return Boolean.TRUE.equals(webClient.get()
                      .uri("/api/users/{userId}/validate", userId)
                      .retrieve()
                      .bodyToMono(Boolean.class)
                      .block());
        } catch (WebClientResponseException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("User not found: " + userId);
            } else if (exception.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Invalid request: " + userId);
            }
        }
        return false;
    }
}
