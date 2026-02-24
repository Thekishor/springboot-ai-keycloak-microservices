package com.user_service.controller;

import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam String token) {
        try {
            boolean isActivated = userService.activateUser(token);
            if (isActivated) {
                return ResponseEntity.ok("User profile activated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.GONE)
                        .body("Activation link expired. Please sign up again");
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") String userId) {
        UserResponse userResponse = userService.getById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.existsByKeycloakId(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "message", "User deleted successfully",
                        "status", HttpStatus.NOT_FOUND
                ));
    }

}
