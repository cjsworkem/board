package com.bitc.board.service;

import com.bitc.board.dto.ItemDto;

import java.util.List;

public interface ThymeleafService {
    ItemDto createDate() throws Exception;

    List<ItemDto> createDateList() throws Exception;
}
