package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.*;
import com.qiniu.util.Json;

import java.util.List;

public interface UserService {


    /**
     * 获取用户信息，
     * 1、如果token为空，返回用户在商城的个人信息，
     * 2、如果token不为空，去够力验证用户的token，添加或更新商城的登录状态，返回用户在商城的个人信息
     * @param uid 用户id
     * @param gltoken 够力token
     * @return
     */
    JsonResult selecteUserInfoByUid(Integer uid, String gltoken, JsonResult jsonResult);


    /**
     * 添加用户收货地址
     * @param userAddress 地址模型
     * @return int
     */
    int insertUserAddress(UserAddress userAddress);


    /**
     * 查询用户所有收货地址
     * @param uid 用户id
     * @return 收货地址列表
     */
    List<UserAddress> selectUserAddressByUid(Integer uid);


    /**
     * 查询该用户的的默认地址，如果没有设置返回最新地址
     * @param uid 用户id
     * @return
     */
    UserAddress selectUserAddressByIsDefault(Integer uid);

    /**
     * 修改用户收货地址
     * @param userAddress 收货地址模型
     * @return
     */
    int updateUserAddressById(UserAddress userAddress);


    /**
     * 根据id删除一个收货地址
     * @param id 收货地址id
     * @return
     */
    int deleteUserAddressById(Integer id);


    /**
     * 查询一个收货地址
     * @param id 收货地址id
     * @return
     */
    UserAddress selectUserAddressById(Integer id);

}
