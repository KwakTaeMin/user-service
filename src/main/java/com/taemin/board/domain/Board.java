package com.taemin.board.domain;

import com.taemin.user.common.BaseEntity;
import com.taemin.user.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Title title;
    @Embedded
    private Content content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Board(Title title, Content content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
