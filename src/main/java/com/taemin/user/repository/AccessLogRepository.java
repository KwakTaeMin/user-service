package com.taemin.user.repository;

import com.taemin.user.domain.log.AccessLog;
import com.taemin.user.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
