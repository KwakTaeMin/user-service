package com.taemin.board.dto.response;

import java.time.LocalDateTime;

public record BoardResponse(
    Long boardId,
    Long userId,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){

}
