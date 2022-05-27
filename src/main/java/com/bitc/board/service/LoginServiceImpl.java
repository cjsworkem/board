package com.bitc.board.service;

import com.bitc.board.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    LoginMapper loginMapper;
    @Override
    public int selectUserInfoYn(String userId, String userPw) throws Exception {



        return loginMapper.selectUserInfoYn(userId, userPw);
    }
}
