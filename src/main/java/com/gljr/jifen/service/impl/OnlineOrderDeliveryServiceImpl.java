package com.gljr.jifen.service.impl;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.dao.OnlineOrderDeliveryMapper;
import com.gljr.jifen.pojo.OnlineOrderDelivery;
import com.gljr.jifen.service.OnlineOrderDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OnlineOrderDeliveryServiceImpl implements OnlineOrderDeliveryService {

    @Autowired
    private OnlineOrderDeliveryMapper onlineOrderDeliveryMapper;

    @Override
    public JsonResult insertOnlineExpressById(OnlineOrderDelivery onlineOrderDelivery) {
        JsonResult jsonResult = new JsonResult();
        try {
            if(StringUtils.isEmpty(onlineOrderDelivery.getId())) {
                onlineOrderDeliveryMapper.insert(onlineOrderDelivery);
            }else{
                onlineOrderDeliveryMapper.updateByPrimaryKey(onlineOrderDelivery);
            }
            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }
}
