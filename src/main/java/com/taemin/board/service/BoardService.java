package com.taemin.board.service;

import com.taemin.board.domain.Board;
import com.taemin.board.dto.request.BoardRequest;
import com.taemin.board.repository.BoardRepository;
import com.taemin.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board writeBoard(User user, BoardRequest boardRequest) {
        Board writeBoard = boardRequest.toBoard(user);
        return boardRepository.save(writeBoard);
    }

    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepository.findBoardListPage(pageable);
    }

    public Board readBoard(Long id) {
        return boardRepository.findById(id).orElseThrow();
    }
}
