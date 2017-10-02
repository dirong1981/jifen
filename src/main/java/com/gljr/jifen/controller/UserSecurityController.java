package com.gljr.jifen.controller;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.RandomUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.service.RedisService;
import com.gljr.jifen.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.gljr.jifen.constants.DBConstants.CACHE_USER_PAYMENT_CODE_KEY;

@Controller
@RequestMapping(value = "/v1/users/security")
public class UserSecurityController extends BaseController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisService redisService;

    /**
     * 获取支付码（120秒过期）
     * @return
     */
    @GetMapping(value = "/payment-code")
    @ResponseBody
    public JsonResult getPaymentCode() {
        JsonResult jsonResult = new JsonResult();

        try {
            String uid = this.request.getHeader("uid");
            if (uid == null || uid.equals("")) {
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            } else {
                Map map = new HashMap();
                String paymentCode = Base64.getEncoder().encodeToString((uid + "A"
                        + DateUtils.getShortTimeStamp() + RandomUtil.nextLong(8)).getBytes());
                this.redisService.put(CACHE_USER_PAYMENT_CODE_KEY + uid, paymentCode,120, TimeUnit.SECONDS);
                map.put("data", paymentCode);
                jsonResult.setItem(map);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            }
        } catch (Exception e) {
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }

}
