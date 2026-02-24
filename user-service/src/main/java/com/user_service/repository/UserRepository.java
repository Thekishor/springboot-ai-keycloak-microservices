package com.user_service.repository;

import com.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByActivationToken(String activationToken);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    Boolean existsByKeycloakId(String keycloakId);
}
