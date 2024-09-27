package com.taemin.board.dto.request;

import com.taemin.board.domain.Board;
import com.taemin.board.domain.Content;
import com.taemin.board.domain.Title;
import com.taemin.user.domain.user.User;


public record BoardRequest(
    String title,
    String content
) {
    public Board toBoard(User user) {
        return new Board(Title.of(title), Content.of(content), user);
    }
}
