package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.NumberUtils;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.vo.SettlePeriodStat;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.pojo.StoreExtInfo;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.service.DTChainService;
import com.gljr.jifen.service.MerchantSettleService;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/manager/settle")
public class SettleManagerController extends BaseController {

    @Autowired
    private DTChainService chainService;

    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private MerchantSettleService settleService;

    private JsonResult _process(GatewayResponse response) {
        JsonResult jsonResult = new JsonResult();

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

    @GetMapping("/integral/stat")
    @ResponseBody
    public JsonResult getIntegralStatInfo() {
        return _process(this.chainService.getIntegralStatInfo());
    }

    @GetMapping("/integral/issued-history")
    @ResponseBody
    public JsonResult getIntegralIssuedHistory() {
        return _process(this.chainService.getIntegralIssuedHistory());
    }

    @GetMapping("/integral/recycled-history")
    @ResponseBody
    public JsonResult getIntegralRecycledHistory() {
        return _process(this.chainService.getIntegralRecycledHistory());
    }

    @GetMapping("/unsettles")
    @ResponseBody
    public JsonResult getIntegralUnsettles() {
        return _process(this.chainService.getUnsettledIntegral());
    }

    @GetMapping("/stores/{id}")
    @ResponseBody
    public JsonResult getStoreInfo(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        StoreInfo si = this.storeInfoService.selectStoreInfoById(id);
        if (null == si) {
            jsonResult.setErrorCode(GlobalConstants.OBJECT_NOT_FOUND);
            jsonResult.setMessage("未找到该商户信息");
        } else {
            Map<Object, Object> data = new HashMap<>();
            data.put("store_name", si.getName());
            StoreExtInfo sei = this.storeInfoService.selectStoreExtInfoBySiId(id);
            if (null != sei && !StringUtils.isEmpty(sei.getBankName())) {
                data.put("bank_name", sei.getBankName());
            } else {
                data.put("bank_name", "无");
            }
            if (null != sei && !StringUtils.isEmpty(sei.getBankAccount())) {
                data.put("bank_account", sei.getBankAccount());
            } else {
                data.put("bank_account", "无");
            }
//            data.put("")
            GatewayResponse<SettlePeriodStat> response = this.chainService.getUnsettledPeriodStat(id + 0L,
                    DateUtils.formatToString(DateUtils.yesterday(), "yyyy-MM-dd"), null);
            if (null != response && null != response.getContent()) {
                data.put("current_amount", response.getContent().getIntegral());
                data.put("block_id", response.getContent().getBlockId());
                data.put("settle_to", DateUtils.formatToString(DateUtils.yesterday(), "yyyy-MM-dd"));
            } else {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("读取数链网络内数据失败，请稍后重试");
                return jsonResult;
            }

            jsonResult.setItem(data);
        }
        return jsonResult;
    }

    @GetMapping("/stores/{id}/unsettle/stat")
    @ResponseBody
    public JsonResult getStoreUnsettleStatInfo(@PathVariable("id") Integer id,
                                               @RequestParam("settle_to") String settleTo) {
        JsonResult jsonResult = new JsonResult();

        if (!DateUtils.validate(settleTo, "yyyy-MM-dd")) {
            jsonResult.setErrorCode(GlobalConstants.BAD_REQUEST);
            jsonResult.setMessage("无效的统计区间（截止日）");
            return jsonResult;
        }

        StoreInfo si = this.storeInfoService.selectStoreInfoById(id);
        if (null == si) {
            jsonResult.setErrorCode(GlobalConstants.OBJECT_NOT_FOUND);
            jsonResult.setMessage("未找到该商户信息");
        } else {
            Map<Object, Object> data = new HashMap<>();
            GatewayResponse<SettlePeriodStat> response = this.chainService.getUnsettledPeriodStat(id + 0L, settleTo, null);
            if (null != response && null != response.getContent()) {
                data.put("current_amount", response.getContent().getIntegral());
                data.put("block_id", response.getContent().getBlockId());
            } else {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("读取数链网络内数据失败，请稍后重试");
                return jsonResult;
            }

            jsonResult.setItem(data);
        }
        return jsonResult;
    }

    @PostMapping("/stores/{id}/block/{blockId}/remit")
    public JsonResult remit(@PathVariable("id") Integer id, @PathVariable("blockId") String blockId,
                            @RequestParam("settle_to") String settleTo, @RequestParam("remitDate") String remitDate,
                            @RequestParam(value = "file", required = false) MultipartFile file) {

        JsonResult jsonResult = new JsonResult();
        jsonResult.setErrorCode(GlobalConstants.BAD_REQUEST);

        if (!DateUtils.validate(settleTo, "yyyy-MM-dd")) {
            jsonResult.setMessage("无效的结算截止日期");
            return jsonResult;
        }

        if (!DateUtils.validate(remitDate, "yyyy-MM-dd")) {
            jsonResult.setMessage("无效的结算汇款日期");
            return jsonResult;
        }

        if (null == file || file.isEmpty()) {
            jsonResult.setMessage("请上传汇款回执照片");
            return jsonResult;
        }

        if (DateUtils.diffSeconds(DateUtils.formatToDate(settleTo, "yyyy-MM-dd"),
                DateUtils.yesterday()) < 0) {
            jsonResult.setMessage("无效的结算截止日期");
            return jsonResult;
        }

        if (DateUtils.diffSeconds(DateUtils.formatToDate(settleTo, "yyyy-MM-dd"),
                DateUtils.formatToDate(remitDate, "yyyy-MM-dd")) < 0) {
            jsonResult.setMessage("结算汇款日期应该晚于结算截止日期");
            return jsonResult;
        }

        int aid = NumberUtils.getInt(super.request.getHeader("aid"));
        if (aid < 1) {
            CommonResult.notAllowedOperation(jsonResult);
            return jsonResult;
        }

        jsonResult = this.settleService.remit(id, blockId, settleTo, remitDate, file, aid);
        if (null != jsonResult.getErrorCode() && GlobalConstants.OPERATION_SUCCEED.equals(jsonResult.getErrorCode())) {
            jsonResult = this.settleService.invokeDTChainRemitService(jsonResult.getMessage(), jsonResult);
        }
        return jsonResult;
    }


}
