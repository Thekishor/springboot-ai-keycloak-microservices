package com.api_gateway.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {

    private String userId;
    private String keycloakId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
