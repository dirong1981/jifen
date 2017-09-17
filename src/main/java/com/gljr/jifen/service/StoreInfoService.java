package com.gljr.jifen.service;

import com.gljr.jifen.pojo.StoreExtInfo;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.pojo.StorePhoto;

import java.util.List;

public interface StoreInfoService
{

    int updataStoreInfo(StoreInfo storeInfo);

    StoreInfo selectStoreInfoById(Integer id);

    int addStoreInfo(StoreInfo storeInfo);

    int deleteStoreInfoById(Integer id);

    List<StoreInfo> selectAllShowStoreInfo(Integer sort);

    List<StoreInfo> selectAllStoreInfo();


    int updataStoreExt(StoreExtInfo storeExt);

    StoreExtInfo getStoreExt(Integer id);

    int addStoreExt(StoreExtInfo storeExt);

    int insertStorePhoto(StorePhoto storePhoto);

    List<StorePhoto> selectStorePhotoById(Integer id);

    int updateStroePhoto(StorePhoto storePhoto);

    StoreInfo selectStoreInfoByAid(Integer id);

}
