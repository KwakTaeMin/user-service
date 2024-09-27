package com.taemin.board.dto.request;

import com.taemin.board.domain.Board;
import com.taemin.board.domain.Content;
import com.taemin.board.domain.Title;
import com.taemin.user.domain.user.User;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


public record BoardRequest(
        @NotNull @Length(max = 50) String title,
        @NotNull @Length(max = 1000) String content
) {
    public Board toBoard(User user) {
        return new Board(Title.of(title), Content.of(content), user);
    }
}
