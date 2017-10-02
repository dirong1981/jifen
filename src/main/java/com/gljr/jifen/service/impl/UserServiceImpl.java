package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.UserAddressMapper;
import com.gljr.jifen.dao.UserCreditsMapper;
import com.gljr.jifen.dao.UserExtInfoMapper;
import com.gljr.jifen.dao.UserOnlineMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private UserOnlineMapper userOnlineMapper;

    @Autowired
    private UserCreditsMapper userCreditsMapper;

    @Autowired
    private UserExtInfoMapper userExtInfoMapper;


    @Override
    public int insertUserAddress(UserAddress userAddress) {
        return userAddressMapper.insert(userAddress);
    }

    @Override
    public List<UserAddress> selectUserAddressByUid(Integer uid) {
        UserAddressExample userAddressExample = new UserAddressExample();
        UserAddressExample.Criteria criteria = userAddressExample.or();
        criteria.andUidEqualTo(uid);
        userAddressExample.setOrderByClause("is_default desc");
        return userAddressMapper.selectByExample(userAddressExample);
    }

    @Override
    public UserAddress selectUserAddressByIsDefault(Integer uid) {
        UserAddressExample userAddressExample = new UserAddressExample();
        UserAddressExample.Criteria criteria = userAddressExample.or();
        criteria.andIsDefaultEqualTo(1);
        criteria.andUidEqualTo(uid);
        List<UserAddress> userAddresses = userAddressMapper.selectByExample(userAddressExample);
        //如果不存在返回最新地址
        if(userAddresses.size() == 0){
            UserAddressExample.Criteria criteria1 = userAddressExample.or();
            criteria1.andUidEqualTo(uid);
            userAddressExample.setOrderByClause("id desc");
            List<UserAddress> userAddresses1 = userAddressMapper.selectByExample(userAddressExample);
            if(userAddresses1.size() != 0){
                return userAddresses1.get(0);
            }
        }else{
            return userAddresses.get(0);
        }
        return null;
    }

    @Override
    public int updateUserAddressById(UserAddress userAddress) {
        return userAddressMapper.updateByPrimaryKey(userAddress);
    }

    @Override
    public int deleteUserAddressById(Integer id) {
        return userAddressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public UserAddress selectUserAddressById(Integer id) {
        return userAddressMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int insertUserInfo(UserCredits userCredits, UserExtInfo userExtInfo, UserOnline userOnline) {
        userCreditsMapper.insert(userCredits);

        userExtInfoMapper.insert(userExtInfo);

        userOnlineMapper.insert(userOnline);
        return 0;
    }

    @Override
    public List<UserOnline> selectUserOnlineByUid(Integer uid) {
        UserOnlineExample userOnlineExample = new UserOnlineExample();
        UserOnlineExample.Criteria criteria = userOnlineExample.or();
        criteria.andUidEqualTo(uid);
        return userOnlineMapper.selectByExample(userOnlineExample);
    }

    @Override
    @Transactional
    public int updateUserInfo(UserCredits userCredits, UserOnline userOnline) {
        userCreditsMapper.updateByPrimaryKey(userCredits);

        userOnlineMapper.updateByPrimaryKey(userOnline);
        return 0;
    }

    @Override
    public int insertUserOnline(UserOnline userOnline) {
        return userOnlineMapper.insert(userOnline);
    }

    @Override
    public List<UserExtInfo> selectUserExtInfoByUid(Integer uid) {
        UserExtInfoExample userExtInfoExample = new UserExtInfoExample();
        UserExtInfoExample.Criteria criteria = userExtInfoExample.or();
        criteria.andUidEqualTo(uid);
        return userExtInfoMapper.selectByExample(userExtInfoExample);
    }

    @Override
    public int insertUserExtInfo(UserExtInfo userExtInfo) {
        return userExtInfoMapper.insert(userExtInfo);
    }
}
