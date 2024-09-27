package com.taemin.board.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.taemin.board.domain.Board;
import com.taemin.board.domain.Content;
import com.taemin.board.domain.Title;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record BoardResponse(
    Long boardId,
    Long userId,
    @JsonUnwrapped Title title,
    @JsonUnwrapped Content content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){

    public static BoardResponse of(Board board) {
        return new BoardResponse(board.getId(), board.getUser().getUserId(), board.getTitle(), board.getContent(), board.getCreatedAt(), board.getUpdatedAt());
    }

    public static List<BoardResponse> of(Page<Board> boards) {
        return boards.stream().map(BoardResponse::of).toList();
    }
}
