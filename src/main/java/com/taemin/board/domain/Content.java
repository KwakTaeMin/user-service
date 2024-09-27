package com.taemin.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Content {

    @Column(nullable = false)
    private String content;

    private Content(String content) {
        this.content = content;
    }
}
