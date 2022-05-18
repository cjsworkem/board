package com.bitc.board.common;

import com.bitc.board.dto.BoardFileDto;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class FileUtils {
    public List<BoardFileDto> parseFileInfo(int boardIdx, MultipartHttpServletRequest multiUploadFiles) throws Exception{
//        ObjectUtils 는 org.springframework.util.objectUtils 를 사용
//        isEmpty()를 사용하여 MultipartHttpServletRequest 를 통해서 업로드된 데이터에 파일 데이터가 존제하는지 확인
//        파일 데이터가 없을 경우 null을 반환하고 메서드 종료
        if (ObjectUtils.isEmpty(multiUploadFiles)) {
            return null;
        } //파일데이터 없을시 종료
        
//        반환 타입에 맞는 데이터 객체를 생성,DB에 저장될 내용을 담을 List 타입의 변수
        List<BoardFileDto> fileList = new ArrayList<>();
        
//        현재 시간을 기준으로 해서 새로운 파일명 생성
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd"); // 포맷 설정
        ZonedDateTime current = ZonedDateTime.now(); // 현재지역을 기준으로 현재 시간 가져옴
        String path = "images/" + current.format(format); //실제파일이 저장될 폴더명, 현재 날짜를 기준으로하면 'images/20220518' 이라는 폴더를 생성함
        File file = new File(path);

//        java.io의 file 클래스 타입의 객체를 통해서 지정한 위치에 동일한 이름의 폴더가 존재하는지 확인
        if (file.exists() ==false) {
            file.mkdirs(); //폴더가 없을 경우 생성
        }

//        클라이언트에서 전달된 파일 데이터에서 전체 파일 이름 목록을 가져옴
        Iterator<String> iterator = multiUploadFiles.getFileNames();
//        각각의 파일정보가 저장될 변수
        String newFileName; // 새로 생성된 파일 이름을 저장할 변수
        String originalFileExtension; //파일 확장자명을 저장할 변수
        String contentType; //실제가 가지고있는 미디어 타입을 저장할 변수

        while(iterator.hasNext()) {
//            지정한 파일명을 가지고 있는 데이터 그룹을 모두 가져옴
            List<MultipartFile> List = multiUploadFiles.getFiles(iterator.next());
            
            for(MultipartFile multipartFile : List) {
//                파일 정보의 컨텐츠 타입 정보를 가져옴
                if (multipartFile.isEmpty() ==false ) {
                    contentType = multipartFile.getContentType();
                    if(ObjectUtils.isEmpty(contentType)) {
                        break;
                    }
//                    이미지 파일일 경우 아래의 제어문 실행, 확장자명 설정
                    else {
                        if (contentType.contains("image/jpeg")) {
                            originalFileExtension = ".jpg";
                        } else if (contentType.contains("image/png")) {
                            originalFileExtension = ".png";
                        } else if (contentType.contains("image/gif")) {
                            originalFileExtension = ".gif";
                        } else {
                            break;
                        }
                    } //else
//                    현재 시간을 기준으로 위에서 생성한 확장자와 합하여 고유한 파일명을 생성
                    newFileName = Long.toString(System.nanoTime()) + originalFileExtension;
                    
//                    DB에 저장할수 있는 BoardFileDto 클래스 타입의 객체를 생성하고 파일 정보를 저장함
                    BoardFileDto boardFile = new BoardFileDto();
                    boardFile.setBoardIdx(boardIdx); //현재 게시물의 글번호를 저장
                    boardFile.setFileSize(multipartFile.getSize()); // 파일의 크기
                    boardFile.setOriginalFileName(multipartFile.getOriginalFilename()); //원본파일의 파일명 저장
                    boardFile.setStoredFilePath(path+"/"+newFileName); //서버에 저장될 경로
//                    생성한 폴더의 경로와 System.nanoTime을 사용하여 생성한 고유한 파일명을 합하여 실제파일이 저장되는 전체 경로를 저장함

                    fileList.add(boardFile); //위에서 미리 생성한 반황 타입의 변수에 데이터 저장

//                    File 클래스 타입의 객체 file에 서버에 저장될 파일 전체 경로를 적용
                    file = new File(path + "/" + newFileName);
//                    지정한 위치로 파일을 복사, 실제로 서버에 저장됨
                    multipartFile.transferTo(file);
                }// (multipartFile.isEmpty() ==false )
            } //  for
        } // while
//        위에서 생성된 모든 파일에 대한 정보를 BoardFileDto 클래스 타입의 LIst 객체인 fileList 를 반환
        return fileList;
    } // parseFileInfo




} //FileUtils
