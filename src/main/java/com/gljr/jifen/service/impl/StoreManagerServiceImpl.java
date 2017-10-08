package com.gljr.jifen.service.impl;

import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.RedisService;
import com.gljr.jifen.service.StoreManagerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StoreManagerServiceImpl implements StoreManagerService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminOnlineMapper adminOnlineMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OnlineOrderMapper onlineOrderMapper;


    @Override
    public Integer selectStoreId(String aid){
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andAidEqualTo(Integer.parseInt(aid));

        List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);
        return storeInfos.get(0).getId();
    }

    @Override
    public JsonResult getStoreInfo(String aid, JsonResult jsonResult) {
        try {
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            StoreInfoExample.Criteria criteria = storeInfoExample.or();
            criteria.andAidEqualTo(Integer.parseInt(aid));
            List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);

            if (ValidCheck.validList(storeInfos)) {
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            Map map = new HashMap();
            map.put("data", storeInfos.get(0));
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectAllProduct(String aid, JsonResult jsonResult) {
        try{
            ProductExample productExample = new ProductExample();
            ProductExample.Criteria criteria = productExample.or();
            criteria.andStatusNotEqualTo(DBConstants.ProductStatus.DELETED.getCode());
            criteria.andSiIdEqualTo(selectStoreId(aid));
            productExample.setOrderByClause("id desc");

            List<Product> products = productMapper.selectByExample(productExample);
            //获取商户名称
            if(!ValidCheck.validList(products)){
                for (Product product : products){
                    StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(product.getSiId());
                    if(ValidCheck.validPojo(storeInfo)){
                        product.setStoreName("已删除");
                    }else {
                        product.setStoreName(storeInfo.getName());
                    }
                }
            }

            Map map = new HashMap();
            map.put("data", products);


            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult login(Admin admin, JsonResult jsonResult) {
        try {
            AdminExample adminExample = new AdminExample();
            AdminExample.Criteria criteria = adminExample.or();
            criteria.andUsernameEqualTo(admin.getUsername());
            List<Admin> admins = adminMapper.selectByExample(adminExample);

            if(ValidCheck.validList(admins)){
                jsonResult.setMessage("用户名不存在");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else {
                Admin selectAdmin = admins.get(0);
                if(selectAdmin.getAccountType() == DBConstants.OwnerType.MERCHANT.getCode()) {
                    if (selectAdmin.getPassword().equals(Md5Util.md5(admin.getPassword() + selectAdmin.getSalt()))) {
                        //生成32位token的key
                        String key = StrUtil.randomKey(32);


                        //生成token中的数据
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", selectAdmin.getId());
                        jsonObject.put("username", selectAdmin.getUsername());
                        jsonObject.put("accountType", selectAdmin.getAccountType());

                        //生成token
                        String token = JwtUtil.createJWT(GlobalConstants.GLJR_PREFIX, jsonObject.toString(), key, GlobalConstants.TOKEN_FAILURE_TIME);

                        AdminOnlineExample adminOnlineExample = new AdminOnlineExample();
                        AdminOnlineExample.Criteria criteria1 = adminOnlineExample.or();
                        criteria1.andAidEqualTo(selectAdmin.getId());
                        criteria1.andClientTypeEqualTo(DBConstants.ClientType.WEB.getCode());

                        List<AdminOnline> adminOnlines = adminOnlineMapper.selectByExample(adminOnlineExample);

                        if (ValidCheck.validList(adminOnlines)) {
                            AdminOnline adminOnline = new AdminOnline();
                            adminOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));
                            adminOnline.setToken(key);
                            adminOnline.setAid(selectAdmin.getId());
                            adminOnline.setClientType(DBConstants.ClientType.WEB.getCode());

                            adminOnlineMapper.insert(adminOnline);
                        } else {
                            AdminOnline adminOnline = adminOnlines.get(0);
                            adminOnline.setToken(key);
                            adminOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));

                            adminOnlineMapper.updateByPrimaryKey(adminOnline);
                        }

                        //查询商户id
                        StoreInfoExample storeInfoExample = new StoreInfoExample();
                        StoreInfoExample.Criteria criteria2 = storeInfoExample.or();
                        criteria2.andAidEqualTo(selectAdmin.getId());

                        List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);

                        if(ValidCheck.validList(storeInfos)){
                            CommonResult.noObject(jsonResult);
                            return jsonResult;
                        }

                        StoreInfo storeInfo = storeInfos.get(0);

                        String permission = GlobalConstants.ONLINE_STORE_ADMIN_PERMISSION;

                        Map map = new HashMap();
                        map.put("aid", selectAdmin.getId() + "");
                        map.put("username", selectAdmin.getUsername());
                        map.put("accountType", selectAdmin.getAccountType() + "");
                        map.put("permission", permission);
                        map.put("tokenKey", key);
                        map.put("storeId", storeInfo.getId()+"");

                        //把用户信息存入jedis
                        this.redisService.put("admin_" + selectAdmin.getId(), JsonUtil.toJson(map));

                        map = new HashMap();
                        map.put("aid", selectAdmin.getId() + "");
                        map.put("username", selectAdmin.getUsername());
                        map.put("token", token);
                        map.put("permission", permission);

                        CommonResult.success(jsonResult);
                        jsonResult.setItem(map);
                    } else {
                        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                        jsonResult.setMessage("密码错误！");
                    }
                }else {
                    jsonResult.setMessage("账户类型错误！");
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    return jsonResult;
                }
            }
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectOnlineOrders(String aid, JsonResult jsonResult) {

        try {
            Map<String, String> tokenMap = this.redisService.getMap("admin_" + aid, String.class);

            String siId = tokenMap.get("storeId");
            OnlineOrderExample onlineOrderExample = new OnlineOrderExample();
            OnlineOrderExample.Criteria criteria = onlineOrderExample.or();
            criteria.andSiIdEqualTo(Integer.parseInt(siId));

            List<OnlineOrder> onlineOrders = onlineOrderMapper.selectByExample(onlineOrderExample);

            Map map = new HashMap();
            map.put("data", onlineOrders);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }
}
