package com.gljr.jifen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gljr.jifen.common.*;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.vo.GouliUserId;
import com.gljr.jifen.common.dtchain.vo.GouliUserInfo;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.DTChainService;
import com.gljr.jifen.service.RedisService;
import com.gljr.jifen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private OnlineOrderMapper onlineOrderMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private DTChainService chainService;

    @Autowired
    private RedisService redisService;

    @Override
    public JsonResult selecteUserInfoByUid(Integer uid, String gltoken, JsonResult jsonResult) {
        String realName;
        Integer totalValue;
        Integer validValue;
        String phone;
        String walletAddress;
        Integer viewType;
        String token = "";
        Integer readMessage;
        String token_key = StrUtil.randomKey(32);

        try {

            //带够力token，新用户生成在线状态，老用户更新商城token，不带够力token，返回商城用户信息
            if(!StringUtils.isEmpty(gltoken)){
                //去沟里验证token

//                GatewayResponse<GouliUserId> gouliUserId = chainService.getUserId(gltoken);
//
//                if (null == gouliUserId || gouliUserId.getCode() != 200) {
//                    CommonResult.userNotExit(jsonResult);
//                    return jsonResult;
//                } else {
//                    CommonResult.success(jsonResult);
//                }
//
//                uid = Integer.parseInt(gouliUserId.getContent().getId() + "");

                uid  = 15470;
                UserOnlineExample userOnlineExample = new UserOnlineExample();
                UserOnlineExample.Criteria criteria = userOnlineExample.or();
                criteria.andUidEqualTo(uid);

                List<UserOnline> userOnlines = userOnlineMapper.selectByExample(userOnlineExample);
                if(ValidCheck.validList(userOnlines)){
                    //第一次进入商城
                    UserOnline userOnline = new UserOnline();
                    userOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));
                    userOnline.setUid(uid);
                    userOnline.setToken(token_key);

                    userOnlineMapper.insert(userOnline);
                }else{
                    //不是第一次，更新token，修改登陆时间
                    UserOnline userOnline = userOnlines.get(0);
                    userOnline.setToken(token_key);
                    userOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));

                    userOnlineMapper.updateByPrimaryKey(userOnline);
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("uid", uid);
                jsonObject.put("role", "USER");

                //生成token
                token = JwtUtil.createJWT(GlobalConstants.GLJR_PREFIX, jsonObject.toString(), token_key, 60*60*24*365);

                Map map = new HashMap();
                map.put("token", token);
                map.put("uid", uid);
                map.put("tokenKey", token_key);

                this.redisService.put("user_" + uid, JsonUtil.toJson(map));
            }


            //获取用户信息
            UserCreditsExample userCreditsExample = new UserCreditsExample();
            UserCreditsExample.Criteria criteria = userCreditsExample.or();
            criteria.andOwnerIdEqualTo(uid);
            criteria.andOwnerTypeEqualTo(DBConstants.OwnerType.CUSTOMER.getCode());

            UserCredits userCredits = this.userCreditsMapper.getUserCredits(uid);
            if(null == userCredits){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }

            UserExtInfoExample userExtInfoExample = new UserExtInfoExample();
            UserExtInfoExample.Criteria criteria1 = userExtInfoExample.or();
            criteria1.andUidEqualTo(uid);
            List<UserExtInfo> userExtInfos = userExtInfoMapper.selectByExample(userExtInfoExample);
            UserExtInfo userExtInfo = userExtInfos.get(0);

            if(ValidCheck.validList(userExtInfos)){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }

            //判断是否有未读消息
            MessageExample messageExample = new MessageExample();
            MessageExample.Criteria criteria2 = messageExample.or();
            criteria2.andUidEqualTo(uid);
            criteria2.andReadStatusEqualTo(0);
            List<Message> messages = messageMapper.selectByExample(messageExample);
            if(ValidCheck.validList(messages)){
                readMessage = 0;
            }else {
                readMessage = 1;
            }

            totalValue = userCredits.getIntegral();
            validValue = userCredits.getFrozenIntegral();
            walletAddress = userCredits.getWalletAddress();
            phone = userExtInfo.getCellphone();
            viewType = userExtInfo.getViewType();

            OnlineOrderExample onlineOrderExample = new OnlineOrderExample();
            OnlineOrderExample.Criteria criteria3 = onlineOrderExample.or();
            criteria3.andStatusEqualTo(DBConstants.OrderStatus.UNPAID.getCode());
            criteria3.andUidEqualTo(uid);

            long count = onlineOrderMapper.countByExample(onlineOrderExample);


            Map map = new HashMap();
            map.put("totalValue", totalValue);
            map.put("validValue", validValue);
            map.put("walletAddress", walletAddress);
            map.put("phone", phone);
            map.put("viewType", viewType);
            map.put("token", token);
            map.put("readMessage", readMessage);
            map.put("unpaid", Integer.parseInt(count + ""));
            map.put("uid", uid);
            //map.put("url", "http://www.gouli.com");

            jsonResult.setItem(map);


            CommonResult.success(jsonResult);


        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }





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

}
