package com.api_gateway.user;

import com.api_gateway.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final WebClient webClient;

    public Mono<Boolean> isValidUser(String userId) {
        log.info("Calling User validate Api for userId: {}", userId);
        return webClient.get()
                .uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, exception -> {
                    if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new UserNotFoundException("User not found: " + userId));
                    } else if (exception.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new RuntimeException("Invalid request: " + userId));
                    } else {
                        return Mono.error(exception);
                    }
                });
    }

    public Mono<UserResponse> registerUser(UserRequest userRequest) {
        log.info("Calling User registration Api: {}", userRequest.getEmail());
        return webClient.post()
                .uri("/api/users/register")
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, exception -> {
                    if (exception.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new IllegalArgumentException("Invalid user data: " + userRequest.getEmail()));
                    } else if (exception.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                        return Mono.error(new RuntimeException("Internal server error: " + exception.getMessage()));
                    } else {
                        return Mono.error(exception);
                    }
                });
    }
}
