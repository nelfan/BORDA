package com.softserve.borda.services.impl;

import com.softserve.borda.entities.BoardList;
import com.softserve.borda.services.BoardListService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardListServiceImpl implements BoardListService {

    @Override
    public List<BoardList> getAll() {
        return null;
    }

    @Override
    public BoardList getBoardListById(Long id) {
        return null;
    }

    @Override
    public BoardList createOrUpdate(BoardList boardList) {
        return null;
    }

    @Override
    public void deleteBoardListById(Long id) {

    }
}
