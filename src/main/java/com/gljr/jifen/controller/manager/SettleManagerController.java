package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.vo.IntegralIssuedHistory;
import com.gljr.jifen.common.dtchain.vo.IntegralStatInfo;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.service.DTChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/manager/settle")
public class SettleManagerController extends BaseController {

    @Autowired
    private DTChainService chainService;

    @GetMapping("/integral/stat")
    @ResponseBody
    public JsonResult getIntegralStatInfo() {
        JsonResult jsonResult = new JsonResult();

        GatewayResponse<IntegralStatInfo> response = this.chainService.getIntegralStatInfo();
        if (null == response || null == response.getContent()) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage("上游服务未能正确响应");
        } else {
            Map<Object, Object> dataMap = new HashMap<>();
            dataMap.put("data", response.getContent());
            jsonResult.setItem(dataMap);
        }

        return jsonResult;
    }

    @GetMapping("/integral/issued-history")
    @ResponseBody
    public JsonResult getIntegralIssuedHistory() {
        JsonResult jsonResult = new JsonResult();

        GatewayResponse<List<IntegralIssuedHistory>> response = this.chainService.getIntegralIssuedHistory();
        if (null == response || null == response.getContent()) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage("上游服务未能正确响应");
        } else {
            Map<Object, Object> dataMap = new HashMap<>();
            dataMap.put("data", response.getContent());
            jsonResult.setItem(dataMap);
        }

        return jsonResult;
    }

}
