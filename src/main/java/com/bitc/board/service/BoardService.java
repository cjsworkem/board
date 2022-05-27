package com.bitc.board.service;

import com.bitc.board.dto.BoardDto;
import com.bitc.board.dto.BoardFileDto;
import com.github.pagehelper.Page;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface BoardService {

    public List<BoardDto> selectBoardList() throws Exception;

    void insertBoard(BoardDto board, MultipartHttpServletRequest multiUplaodFiles) throws Exception;

    void insertBoard(String createId, String title, String contents) throws Exception;

    BoardDto selectBoardDetail(int boardIdx) throws Exception;

    void deleteBoard(int boardIdx) throws Exception;

    void updateBoard(BoardDto board) throws Exception;

    BoardFileDto selectBoardFileInfo(int idx, int boardIdx) throws Exception;

    Page<BoardDto> selectBoardListPaging(int pageNum) throws Exception;
}
