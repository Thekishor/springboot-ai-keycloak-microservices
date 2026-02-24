package com.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequest {

    @NotBlank(message = "Firstname is required")
    @Size(min = 3, max = 100, message = "Firstname must be between 3 and 100 characters")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    @Size(min = 3, max = 100, message = "Lastname must be between 3 and 100 characters")
    private String lastName;

    private String keycloakId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be have at least 8 characters")
    private String password;
}
