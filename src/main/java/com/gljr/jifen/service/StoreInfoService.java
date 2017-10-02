package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.StoreExtInfo;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.pojo.StorePhoto;

import java.util.List;

public interface StoreInfoService
{

    int updataStoreInfo(StoreInfo storeInfo);

    StoreInfo selectStoreInfoById(Integer id);

    int addStoreInfo(StoreInfo storeInfo, Admin admin, Integer random);

    int deleteStoreInfoById(StoreInfo storeInfo, Admin admin);

    List<StoreInfo> selectAllShowStoreInfo(Integer code, Integer sort);

    List<StoreInfo> selectAllStoreInfo();

    List<StoreInfo> selectStroreInfoByKeyword(String keyword);

    int insertStorePhoto(StorePhoto storePhoto);

    List<StorePhoto> selectStorePhotoById(Integer id);

    StoreInfo selectStoreInfoByAid(Integer id);

    Long selectStorePhotoCountBySiId(Integer siid);

}
