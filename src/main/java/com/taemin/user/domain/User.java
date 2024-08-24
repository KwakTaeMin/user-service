package com.taemin.user.domain;

import com.taemin.user.common.BaseEntity;
import com.taemin.user.type.Role;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String email;
    private String profile;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Builder
    public User(String name, String email, String profile, Role role) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.role = role;
    }
}
