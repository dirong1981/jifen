package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.StoreExtInfoMapper;
import com.gljr.jifen.dao.StoreInfoMapper;
import com.gljr.jifen.dao.StorePhotoMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class StoreInfoServiceImpl implements StoreInfoService {


    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private StoreExtInfoMapper storeExtInfoMapper;

    @Autowired
    private StorePhotoMapper storePhotoMapper;


    @Override
    public int updataStoreInfo(StoreInfo storeInfo) {
        return storeInfoMapper.updateByPrimaryKeySelective(storeInfo);
    }

    @Override
    public StoreInfo selectStoreInfoById(Integer id) {
        return storeInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int addStoreInfo(StoreInfo storeInfo) {
        return storeInfoMapper.insert(storeInfo);
    }

    @Override
    public int deleteStoreInfoById(Integer id) {
        return storeInfoMapper.deleteByPrimaryKey(id);
    }


    @Override
    public List<StoreInfo> selectAllShowStoreInfo(Integer sort) {
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andStatusEqualTo(new Byte("1"));
        //设置排序
        if(sort == 0){
            storeInfoExample.setOrderByClause("id desc");
        }else if (sort == 1){
            criteria.andPayStatusEqualTo(new Byte("1"));
            storeInfoExample.setOrderByClause("id desc");
        }else if (sort == 2){
            criteria.andPayStatusEqualTo(new Byte("0"));
            storeInfoExample.setOrderByClause("id desc");
        }
        return storeInfoMapper.selectByExample(storeInfoExample);
    }

    @Override
    public List<StoreInfo> selectAllStoreInfo() {
        return storeInfoMapper.selectByExample(null);
    }

    @Override
    public int updataStoreExt(StoreExtInfo storeExt) {
        return storeExtInfoMapper.updateByPrimaryKeySelective(storeExt);
    }

    @Override
    public StoreExtInfo getStoreExt(Integer id) {
        return storeExtInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int addStoreExt(StoreExtInfo storeExt) {
        return storeExtInfoMapper.insertSelective(storeExt);
    }

    @Override
    public int insertStorePhoto(StorePhoto storePhoto) {
        return storePhotoMapper.insert(storePhoto);
    }

    @Override
    public List<StorePhoto> selectStorePhotoById(Integer id) {
        StorePhotoExample storePhotoExample = new StorePhotoExample();
        StorePhotoExample.Criteria criteria = storePhotoExample.or();
        criteria.andSiIdEqualTo(id);
        return storePhotoMapper.selectByExample(storePhotoExample);
    }

    @Override
    public int updateStroePhoto(StorePhoto storePhoto) {
        return storePhotoMapper.updateByPrimaryKey(storePhoto);
    }

    @Override
    public StoreInfo selectStoreInfoByAid(Integer id) {
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andAidEqualTo(id);
        return storeInfoMapper.selectByExample(storeInfoExample).get(0);
    }


}
