package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Board;
import com.softserve.borda.services.BoardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Override
    public List<Board> getAll() {
        return null;
    }

    @Override
    public Board getBoardById(Long id) {
        return null;
    }

    @Override
    public Board createOrUpdate(Board board) {
        return null;
    }

    @Override
    public void deleteBoardById(Long id) {

    }
}
