package com.taemin.board.api;

import com.taemin.board.domain.Board;
import com.taemin.board.dto.request.BoardRequest;
import com.taemin.board.dto.response.BoardResponse;
import com.taemin.board.service.BoardService;
import com.taemin.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> writeBoard(@AuthenticationPrincipal User user, @RequestBody BoardRequest boardRequest) {
        Board board = boardService.writeBoard(user, boardRequest);
        return ResponseEntity.ok(BoardResponse.of(board));
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoardPage(@PageableDefault Pageable pageable) {
        Page<Board> boards = boardService.getBoardList(pageable);
        return ResponseEntity.ok(BoardResponse.of(boards));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> readBoard(@PathVariable Long boardId) {
        Board board = boardService.readBoard(boardId);
        return ResponseEntity.ok(BoardResponse.of(board));
    }
}
