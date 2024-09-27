package com.taemin.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Title {

    @Column(nullable = false)
    private String title;

    private Title(String title) {
        this.title = title;
    }
}
