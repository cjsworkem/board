package com.bitc.board.service;

import com.bitc.board.common.FileUtils;
import com.bitc.board.dto.BoardDto;
import com.bitc.board.dto.BoardFileDto;
import com.bitc.board.mapper.BoardMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

// @Service : 지정한 interface 를 구현하여 실제 동작하는 클래스를 의미, @Autowired 로 스프링프레임워크가 자동으로 객체를 생성하는 클래스임을 나타냄
@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private FileUtils fileUtils;

    @Override
    public List<BoardDto> selectBoardList() throws Exception {
        return boardMapper.selectBoardList();
    }

//    클라이언트에서 전달한 데이터를 Mapper 를 이용하여 데이터베이스에 저장
//    클라이언트에서 전달한 파일 데이터를 분석하여 실제 파일을 서버의 특정 영역에 저장하고, 파일에 관련된 내용을 데이터베이스에 저장
//    실제 파일을 데이터베이스에 저장하면 데이터 베이스의 용량을 너무 많이 사용하기떄문에 실제 파일은 서버의 특정 영역 혹은 다른 서버에 저장하고, 해당 파일에 관련된 내용만 데이터 베이스에 저장하여 서버를 최적화함
    @Override
    public void insertBoard(BoardDto board, MultipartHttpServletRequest multiUplaodFiles) throws Exception {
//       1. 파일 내용 확인
//       2. 실제 서버의 특정 영역에 저장
//       2.1 FileUtils 클래스를 생성하여 처리
//       3. 파일 관련 내용을 DB에 저장

//      게시물을 데이터에 베이스에 저장
        boardMapper.insertBoard(board);
//      클라이언트에서 전달받은 파일 데이터를 서버의 특정여역에 복사하고 데이터 베이스에 저장할 수있는 데이터로 변환
        List<BoardFileDto> fileList = fileUtils.parseFileInfo(board.getBoardIdx(), multiUplaodFiles);

//      파일 정보가 존재하면 데이터베이스에 첨부 파일 데이터를 저장
        if (CollectionUtils.isEmpty(fileList) == false) {
            boardMapper.insertBoardFileList(fileList);
        }
//          1. 파일 내용 확인
////        ObjectUtils 는 스프링 프레임워크에서 제공하는 라이브러리
////        .isEmpty() 메서드는 지정한 데이터가 비어었으면 true, 비어있지않으면 false 를 반환
//        if(ObjectUtils.isEmpty(multiUplaodFiles)==false) {
////            MultipartHttpServletRequest 에서 getFileNames()메서드를 지원하여 전달된 모든 파일에 대한 이름을 Iterator 데이터 타입으로 반환함,배열임.
////            자바에서 반복자(Iterator)를 사용하는 이유는 인터페이스를 사용하는 것과 비슷함
////            반복자를 사용하면 실제 데이터를 다룰 떄 사용하는 방법이 모두 동일하게 통일됨(list,array 두 방식을 통일함)
//            Iterator<String> iterator = multiUplaodFiles.getFileNames();
//            String name;
////            hasNext() 다음객체가 존재하는지 확인 후 있으면 true, 없으면 false
//
//            while(iterator.hasNext()) {
//                name=iterator.next(); // next() : 해당 객체에 대한 정보를 가져옴
//                System.out.println("file tag name: " + name);
////                MultipartFile 타입으로 생성되어 있기때문에 여러개의 파일 정보가 있을 수 있어 모든 파일 내용을 가져와서 list 타입의 변수에 저장함
//                List<MultipartFile> list = multiUplaodFiles.getFiles(name);
////                List타입으로 저장된 내용을 풀어서 각각의 파일 정보를 화면에 출력함
//                for (MultipartFile multipartFile : list) {
//                    System.out.println("start file information");
//                    System.out.println("file name: " + multipartFile.getOriginalFilename());
//                    System.out.println("file size:" + multipartFile.getSize());
//                    System.out.println("file content type: " + multipartFile.getContentType());
//                    System.out.println("end File information\n");
//                }//for
//            }//while
//        }//if
    }

    @Override
    public void insertBoard(String createId, String title, String contents) throws Exception {
        BoardDto board = new BoardDto();
        board.setCreateId(createId);
        board.setTitle(title);
        board.setContents(contents);
        boardMapper.insertBoard(board);
    }

    
//    게시물 볼때마다 조회수를 증가시켜줌
    @Override
    public BoardDto selectBoardDetail(int boardIdx) throws Exception {
        boardMapper.updateHitCount(boardIdx);
        BoardDto board = boardMapper.selectBoardDetail(boardIdx);
//        게시물 번호를 기준으로하여 file 테이블에서 첨부파일을 목록을 불러옴
        List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
//        불러온 첨부파일 목록을 기존의 BoardDto 클래스 타입의 객체 board에 추가함
        board.setFileList(fileList);

        return board;
    }

    @Override
    public void deleteBoard(int boardIdx) throws Exception {
        boardMapper.deleteBoard(boardIdx);

    }

    @Override
    public void updateBoard(BoardDto board) throws Exception {
        boardMapper.updateBoard(board);

    }

    @Override
    public BoardFileDto selectBoardFileInfo(int idx, int boardIdx) throws Exception {
        return boardMapper.selectBoardFileInfo(idx,boardIdx);
    }

    @Override
    public Page<BoardDto> selectBoardListPaging(int pageNum) throws Exception {
        PageHelper.startPage(pageNum,5);
        return boardMapper.selectBoardListPaging();
    }


}
