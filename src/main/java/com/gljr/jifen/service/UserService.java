package com.gljr.jifen.service;

import com.gljr.jifen.pojo.UserAddress;

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


}
