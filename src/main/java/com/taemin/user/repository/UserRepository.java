package com.taemin.user.repository;

import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(OAuthId oauthId);
}
