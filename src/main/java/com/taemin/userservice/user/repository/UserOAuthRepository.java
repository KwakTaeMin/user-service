package com.taemin.userservice.user.repository;

import com.taemin.userservice.user.domain.User;
import com.taemin.userservice.user.domain.UserOAuth;
import com.taemin.userservice.user.type.OAuthProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOAuthRepository extends JpaRepository<UserOAuth, Long> {
    Optional<UserOAuth> findByUserAndOAuthProvider(User user, OAuthProvider oAuthProvider);
}
