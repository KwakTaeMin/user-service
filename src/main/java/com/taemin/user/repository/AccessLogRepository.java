package com.taemin.user.repository;

import com.taemin.user.domain.log.AccessLog;
import com.taemin.user.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByUser(User user);
}
