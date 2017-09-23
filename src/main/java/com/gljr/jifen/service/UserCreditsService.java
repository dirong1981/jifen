package com.gljr.jifen.service;

import com.gljr.jifen.pojo.UserCredits;

import java.util.List;

public interface UserCreditsService {


    /**
     * 插入用户信息
     * @param userCredits
     * @return
     */
    int insertUserCredits(UserCredits userCredits);


    /**
     * 查询一个用户信息
     * @param uid
     * @return
     */
    List<UserCredits> selectUserCreditsByUid(int uid);

    /**
     * 更新一个用户信息
     * @param userCredits
     * @return
     */
    int updateUserCreditsById(UserCredits userCredits);

}
