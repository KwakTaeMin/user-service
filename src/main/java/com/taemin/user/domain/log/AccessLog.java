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
    @CreatedDate
    private LocalDateTime accessAt;
    private IP ip;
    @Column(nullable = false)
    private String userAgent;
    private String deviceId;

    private AccessLog(User user, AccessType accessType, LocalDateTime accessAt, IP ip, String userAgent, String deviceId) {
        this.user = user;
        this.accessType = accessType;
        this.accessAt = accessAt;
        this.ip = ip;
        this.userAgent = userAgent;
        this.deviceId = deviceId;
    }

    public static AccessLog of(
        User user,
        AccessType accessType,
        LocalDateTime loginTime,
        IP ip,
        String userAgent,
        String deviceId
    ) {
        return new AccessLog(user, accessType, loginTime, ip, userAgent, deviceId);
    }
}
