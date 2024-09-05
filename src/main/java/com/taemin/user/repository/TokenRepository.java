package com.taemin.user.repository;

import com.taemin.user.domain.token.AccessToken;
import com.taemin.user.domain.token.Token;
import com.taemin.user.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByAccessToken(AccessToken accessToken);

    void deleteByUser(User user);
}
