package com.user_service.dto;

import lombok.*;
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
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
