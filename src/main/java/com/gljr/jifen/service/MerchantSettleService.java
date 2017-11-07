package com.gljr.jifen.service;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.vo.SettlePeriodStat;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.MerchantSettleMapper;
import com.gljr.jifen.pojo.MerchantSettlement;
import com.gljr.jifen.pojo.StoreExtInfo;
import com.gljr.jifen.pojo.StoreInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class MerchantSettleService {

    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private DTChainService chainService;

    @Autowired
    private MerchantSettleMapper settleMapper;

    public JsonResult invokeDTChainRemitService(String blockId, JsonResult jsonResult) {
        GatewayResponse response = this.chainService.settleRemit(blockId);
        if (null == response || response.getCode() != 200) {
            jsonResult.setMessage("通知数链网络进行结算时发生了错误，请联系管理员");
            return jsonResult;
        }

        jsonResult = new JsonResult();
        return jsonResult;
    }

    @Transactional
    public JsonResult remit(Integer storeId, String blockId, String settleTo, String remitDate,
                            MultipartFile file, Integer managerId) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setErrorCode(GlobalConstants.BAD_REQUEST);

        MerchantSettlement ms = new MerchantSettlement();

        StoreInfo si = this.storeInfoService.selectStoreInfoById(storeId);
        if (null == si) {
            jsonResult.setMessage("您所结算的商户信息不存在");
            return jsonResult;
        }

        StoreExtInfo sei = this.storeInfoService.selectStoreExtInfoBySiId(si.getId());
        if (null == sei) {
            jsonResult.setMessage("提现账号信息不足，无法完成结算！");
            return jsonResult;
        }

        String _key = this.storageService.uploadToPrivateBucket("console/remit", file);
        if (StringUtils.isEmpty(_key)) {
            CommonResult.uploadFailed(jsonResult);
            return jsonResult;
        }

        ms.setSettlementNo(this.serialNumberService.getNextSettlementNo());
        ms.setMerchantId(si.getId() + 0L);
        ms.setSettleTime(new Date());
        ms.setSettleType(DBConstants.SettleType.BANK_TRANSFER.getCode());
        ms.setBankAccount(sei.getBankAccount());
        ms.setBankReceiptKey(_key);
        ms.setManagerId(managerId + 0L);
        ms.setRemitDate(remitDate);
        ms.setLastBlockId(blockId);
        ms.setStatus(DBConstants.SettleStatus.IN_PROGRESS.getCode());

        GatewayResponse<SettlePeriodStat> respPeriodStat = this.chainService.getUnsettledPeriodStat(si.getId() + 0L, settleTo, blockId);

        if (null == respPeriodStat || respPeriodStat.getCode() != 200 || null == respPeriodStat.getContent()) {
            jsonResult.setMessage("获取数链网络内商户待结算积分出错，请稍后再试");
            return jsonResult;
        }

        SettlePeriodStat ps = respPeriodStat.getContent();
        ms.setCurrentAmount(ps.getIntegral());
        ms.setCurrentFrom(ps.getCurrentFrom());
        ms.setCurrentTo(ps.getCurrentTo());
        ms.setSettleCycle(ps.getCurrentCycle());
        ms.setTotalAmount(this.settleMapper.getTotalSettleIntegrals(si.getId() + 0L) + ms.getCurrentAmount());

        this.settleMapper.initMerchantSettleInfo(ms);

        jsonResult = new JsonResult();
        jsonResult.setMessage(ms.getLastBlockId());
        return jsonResult;
    }

}
