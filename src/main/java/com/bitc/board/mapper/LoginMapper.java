package com.bitc.board.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    int selectUserInfoYn(String userId, String userPw) throws Exception;
}
