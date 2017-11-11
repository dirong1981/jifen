package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.OnlineOrderDelivery;

public interface OnlineOrderDeliveryService {
    JsonResult insertOnlineExpressById(OnlineOrderDelivery onlineOrderDelivery);
}
