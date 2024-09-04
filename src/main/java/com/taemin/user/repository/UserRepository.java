package com.taemin.user.repository;

import com.taemin.user.domain.user.OAuthId;
import com.taemin.user.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(OAuthId oauthId);
}
