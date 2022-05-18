package com.bitc.board.dto;

import lombok.Data;

import java.util.List;

//@Data : lombok 프로젝트를 통해서 사용하는 어노테이션, 직접생성해야하는 getter/setter, toString 메서드를 자동으로 생성해주는 어노테이션
//@getter : lombok 를 통해서 사용하는 어노테이션, getter 메서드를 자동 생성
//@setter : lombok 를 통해서 사용하는 어노테이션, setter 메서드를 자동 생성
@Data
public class BoardDto {
    private int boardIdx;
    private String title;
    private String contents;
    private String createId;
    private String createDate;
    private String updateId;
    private String updateDate;
    private int hitCnt;
//    첨부 파일 파일 목록을 저장할 List<BoardFileDto>객체
    private List<BoardFileDto> fileList;


}
