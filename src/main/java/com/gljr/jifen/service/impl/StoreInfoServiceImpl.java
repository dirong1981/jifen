package com.gljr.jifen.service.impl;


import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.dao.AdminMapper;
import com.gljr.jifen.dao.StoreExtInfoMapper;
import com.gljr.jifen.dao.StoreInfoMapper;
import com.gljr.jifen.dao.StorePhotoMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.RedisService;
import com.gljr.jifen.service.SerialNumberService;
import com.gljr.jifen.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;


import java.util.List;

@Service
public class StoreInfoServiceImpl implements StoreInfoService {


    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private StoreExtInfoMapper storeExtInfoMapper;

    @Autowired
    private StorePhotoMapper storePhotoMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private RedisService redisService;


    @Override
    public int updataStoreInfo(StoreInfo storeInfo) {
        return storeInfoMapper.updateByPrimaryKeySelective(storeInfo);
    }

    @Override
    public StoreInfo selectStoreInfoById(Integer id) {
        return storeInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int addStoreInfo(StoreInfo storeInfo, Admin admin, Integer random) {
        adminMapper.insert(admin);

        storeInfo.setAid(admin.getId());

        storeInfoMapper.insert(storeInfo);

        //更新图片的商户id
        StorePhotoExample storePhotoExample = new StorePhotoExample();
        StorePhotoExample.Criteria criteria = storePhotoExample.or();
        criteria.andSiIdEqualTo(random);

        List<StorePhoto> storePhotos = storePhotoMapper.selectByExample(storePhotoExample);
        if(!ValidCheck.validList(storePhotos)){
            for (StorePhoto storePhoto : storePhotos) {
                storePhoto.setSiId(storeInfo.getId());
                storePhotoMapper.updateByPrimaryKey(storePhoto);
            }
        }

        return 0;
    }

    @Override
    public int deleteStoreInfoById(StoreInfo storeInfo, Admin admin) {
        storeInfoMapper.updateByPrimaryKey(storeInfo);
        adminMapper.updateByPrimaryKey(admin);
        return 0;
    }


    @Override
    public List<StoreInfo> selectAllShowStoreInfo(Integer code, Integer sort) {
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andStatusEqualTo(DBConstants.MerchantStatus.ACTIVED.getCode());
        criteria.andCategoryCodeEqualTo(code);
        //设置排序
        if(sort == 0){
            storeInfoExample.setOrderByClause("id desc");
        }else if (sort == 1){
            criteria.andPayStatusEqualTo(1);
            storeInfoExample.setOrderByClause("id desc");
        }else if (sort == 2){
            criteria.andPayStatusEqualTo(0);
            storeInfoExample.setOrderByClause("id desc");
        }
        return storeInfoMapper.selectByExample(storeInfoExample);
    }

    @Override
    public List<StoreInfo> selectAllStoreInfo() {
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andStatusNotEqualTo(DBConstants.MerchantStatus.DELETED.getCode());
        storeInfoExample.setOrderByClause("id desc");
        return storeInfoMapper.selectByExample(storeInfoExample);
    }

    @Override
    public List<StoreInfo> selectStroreInfoByKeyword(String keyword) {
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andStatusNotEqualTo(DBConstants.MerchantStatus.DELETED.getCode());
        criteria.andNameLike("%" + keyword + "%");
        return storeInfoMapper.selectByExample(storeInfoExample);
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
    public StoreInfo selectStoreInfoByAid(Integer id) {
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andAidEqualTo(id);
        return storeInfoMapper.selectByExample(storeInfoExample).get(0);
    }

    @Override
    public Long selectStorePhotoCountBySiId(Integer siid) {
        StorePhotoExample storePhotoExample = new StorePhotoExample();
        StorePhotoExample.Criteria criteria = storePhotoExample.or();
        criteria.andSiIdEqualTo(siid);
        return storePhotoMapper.countByExample(storePhotoExample);
    }


}
