package com.gljr.jifen.dao;

import org.apache.ibatis.annotations.Param;

public interface SerialNumberMapper {

    int getNextSystemPermissionCode(@Param("parentCode") int parentCode);

    boolean isStoreSerialCodeExist(@Param("serialCode") String serialCode);

    boolean isValidLocation(@Param("locationCode") Integer locationCode);

    int getNextCategoryCode(@Param("parentCode") int parentCode);

    boolean isTrxCodeExist(@Param("trxCode") String trxCode);

}
