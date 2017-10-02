package com.gljr.jifen.service;

import com.gljr.jifen.pojo.*;

import java.util.List;

public interface UserService {


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


    /**
     * 从沟里获取一个用户信息，添加到用户积分表，用户扩展信息表，用户在线表
     * @param userCredits
     * @param userExtInfo
     * @param userOnline
     * @return
     */
    int insertUserInfo(UserCredits userCredits, UserExtInfo userExtInfo, UserOnline userOnline);


    /**
     * 通过id查询用户在线表是否有信息
     * @param uid
     * @return
     */
    List<UserOnline> selectUserOnlineByUid(Integer uid);

    /**
     * 更新用户积分，登录信息
     * @param userCredits
     * @param userOnline
     * @return
     */
    int updateUserInfo(UserCredits userCredits, UserOnline userOnline);

    /**
     * 添加用户在线表
     * @param userOnline
     * @return
     */
    int insertUserOnline(UserOnline userOnline);

    /**
     * 通过UserCredits的id查询用户信息表中该id对应的数据
     */
    List<UserExtInfo> selectUserExtInfoByUid(Integer id);

    /**
     * 添加一个用户拓展信息
     * @param userExtInfo
     * @return
     */
    int insertUserExtInfo(UserExtInfo userExtInfo);
}
