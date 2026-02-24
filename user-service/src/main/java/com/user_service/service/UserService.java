package com.user_service.service;

import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRequest request);

    boolean activateUser(String activationToken);

    UserResponse getById(String userId);

    Boolean existsByUserId(String userId);

    void deleteUser(String userId);

    Boolean existsByKeycloakId(String keycloakId);
}
