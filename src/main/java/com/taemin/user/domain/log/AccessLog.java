package com.taemin.user.domain.log;

import com.taemin.user.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(value = EnumType.STRING)
    private AccessType accessType;
    private boolean state;
    @CreatedDate
    private LocalDateTime accessAt;
    private IP ip;
    @Column(nullable = false)
    private UserAgent userAgent;

    private AccessLog(User user, AccessType accessType, boolean state, LocalDateTime accessAt, IP ip, UserAgent userAgent) {
        this.user = user;
        this.accessType = accessType;
        this.state = state;
        this.accessAt = accessAt;
        this.ip = ip;
        this.userAgent = userAgent;
    }

    public static AccessLog ofLogin(User user, IP ip, UserAgent userAgent) {
        return new AccessLog(user, AccessType.LOGIN, true, LocalDateTime.now(), ip, userAgent);
    }
}
