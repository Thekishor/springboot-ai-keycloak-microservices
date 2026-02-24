package com.user_service.service;

import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entity.User;
import com.user_service.exception.UserNotFoundException;
import com.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${backend.url}")
    private String backendUrl;

    private final UserRepository userRepository;

    @Override
    public UserResponse register(UserRequest request) {
        log.info("Getting user request: {}", request);
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                log.warn("User already exists with email address: {}", request.getEmail());
                User savedUser = userRepository.findByEmail(request.getEmail());
                return mapUserEntityToUserResponse(savedUser);
            }
            User user = mapUserRequestToUserEntity(request);
            user.setActivationToken(UUID.randomUUID().toString());
            user.setActivationTokenExpiry(LocalDateTime.now().plusHours(24));
            User savedUser = userRepository.save(user);
            log.info("Saved user into database: {}", savedUser);

            log.info("Generate user profile activation link");
            String activationLink = backendUrl + "/activate?token=" + savedUser.getActivationToken();
            log.info("Activation Link for verify user profile: {}", activationLink);
            return mapUserEntityToUserResponse(savedUser);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public boolean activateUser(String activationToken) {
        if (activationToken == null || activationToken.isEmpty()) {
            log.warn("Activation token should not be null or empty");
            throw new RuntimeException("Activation token should not be null or empty");
        }
        User user = userRepository.findByActivationToken(activationToken)
                .orElseThrow(() -> new RuntimeException("Invalid activation token"));

        log.info("User Information: {}", user);
        if (user.getActivationTokenExpiry().isAfter(LocalDateTime.now())) {
            user.setIsActive(true);
            user.setActivationToken(null);
            user.setActivationTokenExpiry(null);
            userRepository.save(user);
            return true;
        } else {
            userRepository.delete(user);
            return false;
        }
    }

    @Override
    public UserResponse getById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id"));
        return mapUserEntityToUserResponse(user);
    }

    @Override
    public Boolean existsByUserId(String userId) {
        return userRepository.existsById(userId);
    }

    @Transactional
    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id"));
        userRepository.delete(user);
    }

    @Override
    public Boolean existsByKeycloakId(String keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }

    private User mapUserRequestToUserEntity(UserRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .keycloakId(request.getKeycloakId())
                .build();
    }

    private UserResponse mapUserEntityToUserResponse(User savedUser) {
        return UserResponse.builder()
                .userId(savedUser.getId())
                .keycloakId(savedUser.getKeycloakId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .isActive(savedUser.getIsActive())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }
}
