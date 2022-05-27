package com.bitc.board.controller;

import com.bitc.board.dto.BoardDto;
import com.bitc.board.dto.BoardFileDto;
import com.bitc.board.service.BoardService;
import com.bitc.board.service.LoginService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class BoardController {

//  @Autowired : 스프링 프레임워크에서 지원하는 어노테이션으로 해당 객체를 사용자가 아닌 스프링 프레임워크에서 객체를 생성한다는 의미
    @Autowired
    private BoardService boardService;

//  @RequestMapping : 스프링 프레임워크의 어노테이션으로 클라이언트가 서버로 접속하기위한 실제 url 주소를 뜻하며
//    해당 이노테이션을 사용한 자바 클래스의 메서드와 url을 1:1로 매칭시킴
//    해당 메서드의 반환 타입은 기본적으로 resource 폴더의 templates 폴더를 루트 폴더로 인식함
//    반환 타입이 String일 경우 templates 폴더 안에있는 html 파일의 이름을 가르킴(기본이 .html이라서 확장자는 무시함)
//    @RequestMapping("/")
//    public String index() throws Exception{
//        return "index";
//    }

//  ModelAndView = 데이터와 사용자 View 객체를 함께 가지고 있는 클래스
//  단순 화면 출력 및 서버에서 받아온 데이터를 출력할 때 사용하는 클래스
//  생성자의 매개변수로 사용자에게 출력할 view 파일을 지정함(경로포함)
//  view로 사용하는 파일을 resource/templates 폴더를 root 폴더로 사용함
//  게시판 전체 목록 출력하기
    @RequestMapping("/board/boardList.do")
    public ModelAndView openBoardList() throws Exception{
        ModelAndView mv = new ModelAndView("/board/boardList");

        List<BoardDto> dataList = boardService.selectBoardList();
//      addObject() : ModelAndView 클래스의 맴버 메소드로 해당 객체의 데이터를 추가하기 위한 메서드
//      첫번째 매개변수로 view에서 사용할 이름을 설정하고, 두번째 매개변수로 실제 데이터를 사용.
//      지정한 view 파일에서 사용하는 데이터 객체의 이름과 첫번째 매개변수의 값을 동일하게 사용해야 함.
        mv.addObject("dataList",dataList);

        return mv;
    }
//  단순히 글쓰기 페이지를 사용자에게 출력하기 위한 부분
    @RequestMapping("/board/writeBoard.do")
//  컨트롤러 클래스에서 @RequestMapping 어노테이션을 사용한 메서드의 반환값을 String을 사용하면 view 파일을 지정한다는 의미
    public String writeBoard() throws Exception{
        return "/board/boardWrite";
    }

////  클라이언트가 전달한 데이터를 DB에 저장하는 로직이 있는 부분 RequestParam 어노테이션을 통해서 받음
////  컨트롤러>서비스>mapper>DB
//    @RequestMapping("/board/insertBoard.do")
////  @RequestParam : 클라이언트에서 서버로 요청 시 전달되는 파라미터 데이터를 뜻함, 한개 or 두개의 정보가 넘어올때는 사용
////  클라이언트의 input 태그의 name속성값을 입력
//    public String insertBoard(@RequestParam("user-id") String createId, @RequestParam("title") String title, @RequestParam("contents") String contents ) throws Exception{
////        BoardDto data = new BoardDto();
////        data.setCreateId(createId);
////        data.setTitle(title);
////        data.setContents(contents);
////        boardService.insertBoard(data);
//
//        boardService.insertBoard(createId,title,contents);
//        return "redirect:/board/boardList.do";
//    }
    
//  매개변수로 사용자가 생성한 클래스 타입을 사용시 클라이언트에서 데이터를 전송하고자 하면 클라이언트의 input 태그에서 name 속성을 사용자가 클래스타입에 맞춰서 이력을 해야 함
//  클라이언트에서 name속성 값을 사용자가 클래스 타입에 맞게 입력하지 않을 경우 데이터가 전달되지 않음
    @RequestMapping("/board/insertBoard.do")
    public String insertBoard(BoardDto board, MultipartHttpServletRequest multiUploadFiles) throws Exception{
//        클라이언트에서 파일 데이터를 전달하면 해당 파일 데이터를 받기 위해서 MultipartHttpServletRequest 객체를 통해서 파일 데이터를 받아옴
//        받아온 파일데이터를 서비스로  전달하여 서비스에서 전달받은 실제 파일 내용을 분석함
        boardService.insertBoard(board,multiUploadFiles);
        return "redirect:/board/boardList.do";
    }
//게시물 상세보기
    @RequestMapping("/board/boardDetail.do")
    public ModelAndView boardDetail(@RequestParam("boardIdx") int boardIdx) throws  Exception {
        ModelAndView mv = new ModelAndView("/board/boardDetail");

        BoardDto board = boardService.selectBoardDetail(boardIdx);
        mv.addObject("board",board);
        return mv;
    }
    
//  redirect : 를 사용 시 해당 "주소"로 서버에 요청함/  redirect 를 안쓰면 해당 html 파일을 요청함.
//게시물 수정하기
    @RequestMapping("/board/updateBoard.do")
    public String updateBoard(BoardDto board) throws  Exception {
        boardService.updateBoard(board);
        return "redirect:/board/boardList.do";
    }
//게시물 삭제하기
    @RequestMapping("/board/deleteBoard.do")
    public String deleteBoard(@RequestParam("boardIdx") int boardIdx) throws Exception {
        boardService.deleteBoard(boardIdx);
        return "redirect:/board/boardList.do";
    }

//    파일다운로드
//    파일 다운로드를 구현하기위해서 매개변수 3가지를 받음
//    HttpServletResponse : 서버에서 클라이언트의 요청에 대한 응답을 하는 객체
    @RequestMapping("/board/downloadBoardFile.do")
    public void downloadBoardFile(@RequestParam("idx") int idx, @RequestParam("boardIdx") int boardIdx, HttpServletResponse response) throws Exception{
//        서비스를 이용하여 파일 정보를 가져옴
        BoardFileDto boardFile = boardService.selectBoardFileInfo(idx,boardIdx);
//        DB 에서 가져온 파일정보가 있는지 확인
        if (ObjectUtils.isEmpty(boardFile) == false) {
            String fileName = boardFile.getOriginalFileName();
//            DB 에서 파일정보를 가져온 후 실제 저장된 파일의 위치를 기반으로 Java.io.File 클래스의 객체를 생성하여 메모리에 실제 파일 정보를 불러와서
//            apache commons.io 라이브러리를 이용하여 해당 파일을 byte 타입의 배열로 변경
//            tcp/ip 네트워크 통신에서 데이터 전송의 기본이 byte[] 타입의 배열로 진행함
            byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));
//            파일 전송을 위해서 통신 타입을 지정함
            response.setContentType("application/octet-stream");
            response.setContentLength(files.length);
//            문자 인코딩 방식을 UTF-8로 지정(지정하지않을시 다운받을시 파일이름이 꺠짐) "attachment; filename 사이에 한칸뛰우는게 중요!
            response.setHeader("Content-Disposition","attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";");
            response.setHeader("Content-Transfer-Encoding","binary");

//            위에서 설정된 방식으로 파일 전송
            response.getOutputStream().write(files); // outputStream 에 파일등록
            response.getOutputStream().flush(); //outputStream 적용, 파일내보내기
            response.getOutputStream().close(); // 닫기
        }
    } //downloadBoardFile

//  주소만 다르게.
    @RequestMapping(value = {"/","/board"})
    public String indexPage() {
        return "index";
    }




    //=================================================================================================================
    //    bsBoard부분
    @RequestMapping("/bsBoard/bsBoardList.do")
    public ModelAndView openBsBoardList(@RequestParam(required = false,defaultValue = "1")int pageNum,HttpServletRequest request) throws Exception{
        ModelAndView mv = new ModelAndView("/bsBoard/bsBoardList");
        PageInfo<BoardDto> dataList = new PageInfo<>(boardService.selectBoardListPaging(pageNum),5);
//        List<BoardDto> dataList = boardService.selectBoardList();
        mv.addObject("dataList",dataList);
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        mv.addObject("userId",userId);

        return mv;
    }


    @RequestMapping("/bsBoard/bsWriteBoard.do")
    public String writeBsBoard() throws Exception{
        return "/bsBoard/bsBoardWrite";
    }

    @RequestMapping("/bsBoard/bsInsertBoard.do")
    public String insertBsBoard(BoardDto board, MultipartHttpServletRequest multiUploadFiles) throws Exception{
        boardService.insertBoard(board,multiUploadFiles);
        return "redirect:/bsBoard/bsBoardList.do";
    }

    @RequestMapping("/bsBoard/bsBoardDetail.do")
    public ModelAndView bsBoardDetail(@RequestParam("boardIdx") int boardIdx) throws  Exception {
        ModelAndView mv = new ModelAndView("/bsBoard/bsBoardDetail");

        BoardDto board = boardService.selectBoardDetail(boardIdx);
        mv.addObject("board",board);
        return mv;
    }

    @RequestMapping("/bsBoard/bsUpdateBoard.do")
    public String updateBsBoard(BoardDto board) throws  Exception {
        boardService.updateBoard(board);
        return "redirect:/bsBoard/bsBoardList.do";
    }
    //게시물 삭제하기
    @RequestMapping("/bsBoard/bsDeleteBoard.do")
    public String deleteBsBoard(@RequestParam("boardIdx") int boardIdx) throws Exception {
        boardService.deleteBoard(boardIdx);
        return "redirect:/bsBoard/bsBoardList.do";
    }

    @RequestMapping("/bsBoard/login")
    public String boardLogin() throws Exception{
        return "/bsBoard/bsBoardLogin";
    }

    @Autowired
    LoginService loginService;
    @RequestMapping("/bsBoard/loginCheck")
    public String boardLoginCheck(@RequestParam("userId") String userId, @RequestParam("userPw") String userPw, HttpServletRequest request) throws Exception{

        int count = loginService.selectUserInfoYn(userId,userPw);
        if (count == 1) {
            HttpSession session = request.getSession();
            session.setAttribute("userId",userId);
            session.setMaxInactiveInterval(300);
            return "redirect:/bsBoard/bsBoardList.do";
        } else {
            return "redirect:/bsBoard/loginFail";
        }
    }

    @RequestMapping("/bsBoard/loginFail")
    public String boardLoginFail() throws Exception{
        return "/bsBoard/bsBoardLoginFail";
    }

    @RequestMapping("/bsBoard/isLoginFalse")
    public String boardIsLoginFalse() throws  Exception{
        return "/bsBoard/bsBoardIsLoginFalse";
    }
    @RequestMapping("/bsBoard/logout")
    public String logout(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        session.removeAttribute("userId");
        session.invalidate();
        return "redirect:/bsBoard/bsBoardList.do";
    }








}
