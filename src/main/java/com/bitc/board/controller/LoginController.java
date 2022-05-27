package com.bitc.board.controller;

import com.bitc.board.dto.UserDto;
import com.bitc.board.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;

    @RequestMapping("/login")
    public String login() throws Exception {
        return "login/login";
    }

//    스프링에서 세션을 사용하기 위해 HttpServletRequest 클래스를 사용함
//    세션정보가 필요한 부분에서는 HttpServletRequest 를 매개변수로 사용해야함!(중요)
    @RequestMapping("/loginCheck")
    public String loginCheck(@RequestParam("userId") String userId, @RequestParam("userPw") String userPw, HttpServletRequest request) throws Exception{
        int count = loginService.selectUserInfoYn(userId,userPw);
        if(count==1) {
//            HttpSession 을 사용하여 실제 세션에 세션 정보를 가져옴
//            setAttribute() 메서드를 사용하여 세션에 정보를 저장함
//            세션정보는 invalidate() 를 사용하여여 모든 정보를 삭제하거나 웹브라우저를 종료하거나 지정된 세션파괴사간이 지나면 삭제됨
            HttpSession session = request.getSession();
            session.setAttribute("userId",userId);
            session.setMaxInactiveInterval(300);
            return "redirect:/login/loginOk";
        } else {
            return "redirect:/login/loginFail";
        }

    }

    @RequestMapping("/loginOk")
    public ModelAndView loginOk(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        UserDto user = new UserDto();
        user.setUserId((String) session.getAttribute("userId"));
        ModelAndView mv = new ModelAndView("/login/loginOk");
        mv.addObject("user",user);
        return mv;
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
//        세션에 저장된 정보 삭제
        session.removeAttribute("userId");
//        모든 세션 정보 삭제(이것만써도 상관없음)
        session.invalidate();
        return "login/logout";
    }

    @RequestMapping("/loginFail")
    public String loginFail() throws Exception{
        return "login/loginFail";
    }

    @RequestMapping("/view")
    public ModelAndView viewPage(HttpServletRequest request) throws Exception{
        ModelAndView mv = new ModelAndView("/login/viewPage");
        HttpSession session = request.getSession();
        UserDto user = new UserDto();
        user.setUserId((String)session.getAttribute("userId"));

        if(user.getUserId()!=null) {
            mv.addObject("userId",user.getUserId());
        }


        return mv;
    }
}
