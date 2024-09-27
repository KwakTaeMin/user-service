package com.taemin.board.dto.request;

import com.taemin.board.domain.Board;
import com.taemin.board.domain.Content;
import com.taemin.board.domain.Title;
import com.taemin.user.domain.user.User;
import jakarta.validation.constraints.NotNull;


public record BoardRequest(
    @NotNull String title,
    @NotNull String content
) {
    public Board toBoard(User user) {
        return new Board(Title.of(title), Content.of(content), user);
    }
}
