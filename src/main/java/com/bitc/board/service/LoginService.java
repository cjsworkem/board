package com.bitc.board.service;

public interface LoginService {
    int selectUserInfoYn(String userId, String userPw) throws Exception;
}
