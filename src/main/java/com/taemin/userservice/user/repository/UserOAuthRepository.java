package com.taemin.userservice.user.repository;

import com.taemin.userservice.user.domain.User;
import com.taemin.userservice.user.domain.UserOAuth;
import com.taemin.userservice.user.type.OAuthProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOAuthRepository extends JpaRepository<UserOAuth, Long> {
    @Query("SELECT u FROM UserOAuth u WHERE u.user = :user AND u.oAuthProvider = :oAuthProvider")
    Optional<UserOAuth> findByUserAndOAuthProvider(@Param("user") User user, @Param("oAuthProvider") OAuthProvider oAuthProvider);
}
