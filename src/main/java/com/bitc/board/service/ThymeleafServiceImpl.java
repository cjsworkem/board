package com.bitc.board.service;

import com.bitc.board.dto.ItemDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThymeleafServiceImpl implements ThymeleafService{
    @Override
    public ItemDto createDate() throws Exception {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명");
            itemDto.setItemNm("테스트 상품");
            itemDto.setPrice(1000);
            itemDto.setRegTime(LocalDateTime.now());

        return itemDto;
    }

    @Override
    public List<ItemDto> createDateList() throws Exception {
        List<ItemDto> itemList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 * i);
            itemDto.setRegTime(LocalDateTime.now());
            itemList.add(itemDto);
        }
        return itemList;
    }
}
