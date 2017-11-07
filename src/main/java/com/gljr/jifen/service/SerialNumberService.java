package com.gljr.jifen.service;

import com.gljr.jifen.common.RandomUtil;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.dao.CouponMapper;
import com.gljr.jifen.dao.SerialNumberMapper;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SerialNumberService {

    private final static Logger LOG = LoggerFactory.getLogger(SerialNumberService.class);

    @Autowired
    private SerialNumberMapper serialNumberMapper;

    @Autowired
    private CouponMapper couponMapper;

    /**
     * 获取下一个系统权限Code
     *
     * @param parentCode 上级Code值
     * @return 可用的系统权限Code
     */
    public int getNextSystemPermissionCode(int parentCode) {
        return this.serialNumberMapper.getNextSystemPermissionCode(parentCode);
    }

    /**
     * 获取分类Code值
     *
     * @param category 分类信息
     * @return 可用的Code值
     */
    public Integer getNextCategoryCode(Category category) {
        return this.serialNumberMapper.getNextCategoryCode(null == category.getParentCode() ? 0 : category.getParentCode());
    }

    /**
     * 产生商铺序列号 （城市代码+时间戳+随机4位数）
     *
     * @param cityCode 城市代码（需详细到区，如450202）
     * @return 商铺序列号
     */
    public String gextNextStoreSerialCode(Integer cityCode) {
        if (null == cityCode || cityCode % 100 == 0
                || !this.serialNumberMapper.isValidLocation(cityCode)) {
            LOG.error("[SNS] Invalid cityCode: {}", cityCode);
            return null;
        }

        cityCode = cityCode / 100;

        String _serialCode = "" + cityCode + DateUtils.getShortTimeStamp() + RandomUtil.nextLong(4);

        while (this.serialNumberMapper.isStoreSerialCodeExist(_serialCode)) {
            _serialCode = "" + cityCode + DateUtils.getShortTimeStamp() + RandomUtil.nextLong(4);
        }

        return _serialCode;
    }

    public String getNextTrxCode(Integer trxType) {
        if (null == trxType || trxType < DBConstants.TrxType.OFFLINE.getCode()
                || trxType > DBConstants.TrxType.REFUND.getCode()) {
            LOG.error("[SNS] Invalid trxType: {}", trxType);
            return null;
        }

        String _trxCode = "" + trxType + DateUtils.getShortTimeStamp() + RandomUtil.nextLong(6);

        while (this.serialNumberMapper.isTrxCodeExist(_trxCode)) {
            _trxCode = "" + trxType + DateUtils.getShortTimeStamp() + RandomUtil.nextLong(6);
        }

        return _trxCode;
    }

    public String getNextSettlementNo() {
        String _settleNo = "" + DateUtils.getShortTimeStamp() + RandomUtil.nextLong(7);
        while(this.serialNumberMapper.isSettleNoExist(_settleNo)) {
            _settleNo = "" + DateUtils.getShortTimeStamp() + RandomUtil.nextLong(7);
        }

        return _settleNo;
    }

}
