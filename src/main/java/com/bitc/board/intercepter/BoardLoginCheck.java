package com.bitc.board.intercepter;

import com.bitc.board.dto.UserDto;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BoardLoginCheck implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        UserDto user = new UserDto();
        user.setUserId((String)session.getAttribute("userId"));

        if (user.getUserId()==null) {
            response.sendRedirect("/bsBoard/isLoginFalse");
            return false;
        } else {

            return true;
        }

    }
}
