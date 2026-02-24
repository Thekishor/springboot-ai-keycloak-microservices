package com.api_gateway.config;

import com.api_gateway.user.UserRequest;
import com.api_gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeyCloakUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        log.info("Bearer token {}", token);

        if (token == null) {
            return chain.filter(exchange);
        }

        UserRequest userRequest = getUserDetails(token);
        log.info("User Request Information from keycloak: {}", userRequest);

        if (userRequest == null || userRequest.getKeycloakId() == null) {
            return chain.filter(exchange);
        }
        String userId = userRequest.getKeycloakId();

        return userService.isValidUser(userId)
                .onErrorResume(error -> Mono.just(false))
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return userService.registerUser(userRequest)
                                .onErrorResume(error -> {
                                    log.error("User sync failed", error);
                                    return Mono.empty();
                                });
                    }
                    return Mono.empty();
                })
                .then(chain.filter(exchange));
    }

    private UserRequest getUserDetails(String token) {
        try {
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            return UserRequest.builder()
                    .email(claims.getStringClaim("email"))
                    .keycloakId(claims.getStringClaim("sub"))
                    .password(UUID.randomUUID().toString())
                    .firstName(claims.getStringClaim("given_name"))
                    .lastName(claims.getStringClaim("family_name"))
                    .build();
        } catch (Exception exception) {
            log.error("Invalid token found: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
}
