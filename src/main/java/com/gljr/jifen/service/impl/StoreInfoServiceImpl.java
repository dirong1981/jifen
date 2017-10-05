package com.gljr.jifen.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.RedisService;
import com.gljr.jifen.service.SerialNumberService;
import com.gljr.jifen.service.StorageService;
import com.gljr.jifen.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CategoryMapper categoryMapper;

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserCreditsMapper userCreditsMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public JsonResult selectAllStoreInfo(JsonResult jsonResult) {
        try {
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            StoreInfoExample.Criteria criteria = storeInfoExample.or();
            criteria.andStatusNotEqualTo(DBConstants.MerchantStatus.DELETED.getCode());
            storeInfoExample.setOrderByClause("id desc");

            List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);

            for (StoreInfo storeInfo : storeInfos){
                CategoryExample categoryExample = new CategoryExample();
                CategoryExample.Criteria criteria1 = categoryExample.or();
                criteria1.andCodeEqualTo(storeInfo.getCategoryCode());

                List<Category> categories = categoryMapper.selectByExample(categoryExample);
                if(ValidCheck.validList(categories)){
                    storeInfo.setCategoryName("已删除");
                }else {
                    storeInfo.setCategoryName(categories.get(0).getName());
                }
            }

            Map map = new HashMap();
            map.put("data", storeInfos);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult startStoreInfo(Integer storeId, JsonResult jsonResult) {

        try {
            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeId);

            if(ValidCheck.validPojo(storeInfo)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            storeInfo.setStatus(DBConstants.MerchantStatus.ACTIVED.getCode());

            storeInfoMapper.updateByPrimaryKey(storeInfo);

            //更新商户管理员的状态
            Admin admin = adminMapper.selectByPrimaryKey(storeInfo.getAid());

            admin.setStatus(DBConstants.AdminAccountStatus.ACTIVED.getCode());

            adminMapper.updateByPrimaryKey(admin);

            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult stopStoreInfo(Integer storeId, JsonResult jsonResult) {
        try {
            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeId);

            if(ValidCheck.validPojo(storeInfo)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            storeInfo.setStatus(DBConstants.MerchantStatus.OFFLINE.getCode());

            storeInfoMapper.updateByPrimaryKey(storeInfo);

            //更新商户管理员的状态
            Admin admin = adminMapper.selectByPrimaryKey(storeInfo.getAid());

            admin.setStatus(DBConstants.AdminAccountStatus.DISABLED.getCode());

            adminMapper.updateByPrimaryKey(admin);

            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult deleteStoreInfo(Integer storeId, JsonResult jsonResult) {
        try {
            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeId);

            if(ValidCheck.validPojo(storeInfo)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            Admin admin = adminMapper.selectByPrimaryKey(storeInfo.getAid());

            if(ValidCheck.validPojo(admin)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            storeInfo.setStatus(DBConstants.MerchantStatus.DELETED.getCode());
            admin.setStatus(DBConstants.AdminAccountStatus.DISABLED.getCode());

            storeInfoMapper.updateByPrimaryKey(storeInfo);
            adminMapper.updateByPrimaryKey(admin);


            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult insertStoreInfo(StoreInfo storeInfo, MultipartFile file, String username, Integer random, JsonResult jsonResult) {
        try {

            //添加商户管理员账号
            AdminExample adminExample = new AdminExample();
            AdminExample.Criteria criteria = adminExample.or();
            criteria.andUsernameEqualTo(username);

            List<Admin> admins = adminMapper.selectByExample(adminExample);
            if(!ValidCheck.validList(admins)){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("管理员账号已存在，请更换！");
                return jsonResult;
            }

            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setCreateTime(new Timestamp(System.currentTimeMillis()));
            admin.setAccountType(DBConstants.AdminAccountType.STORE_ADMIN.getCode());
            admin.setStatus(DBConstants.AdminAccountStatus.INACTIVE.getCode());
            String salt = StrUtil.randomKey(32);
            admin.setSalt(salt);
            admin.setPassword(Md5Util.md5("admin"+salt));



            //上传图片
            if (file != null && !file.isEmpty()) {
                String _key = storageService.uploadToPublicBucket("store", file);
                if (StringUtils.isEmpty(_key)) {
                    CommonResult.uploadFailed(jsonResult);
                    return jsonResult;
                }
                storeInfo.setLogoKey(_key);
            } else {
                storeInfo.setLogoKey("store/default.png");
            }

            adminMapper.insert(admin);

            storeInfo.setStatus(DBConstants.MerchantStatus.INACTIVE.getCode());
            storeInfo.setSerialCode(serialNumberService.gextNextStoreSerialCode(450204));
            storeInfo.setAid(admin.getId());
            storeInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));

            storeInfoMapper.insert(storeInfo);

            //添加商户的积分信息
            UserCredits userCredits = new UserCredits();
            userCredits.setIntegral(0);
            userCredits.setFrozenIntegral(0);
            userCredits.setCreateTime(new Timestamp(System.currentTimeMillis()));
            userCredits.setOwnerId(storeInfo.getId());
            userCredits.setWalletAddress("xxx");
            userCredits.setOwnerType(DBConstants.OwnerType.MERCHANT.getCode());
            userCredits.setFeePaymentLimit(10000);

            userCreditsMapper.insert(userCredits);


            //更新图片的商户id
            StorePhotoExample storePhotoExample = new StorePhotoExample();
            StorePhotoExample.Criteria criteria1 = storePhotoExample.or();
            criteria1.andSiIdEqualTo(random);

            List<StorePhoto> storePhotos = storePhotoMapper.selectByExample(storePhotoExample);
            if(!ValidCheck.validList(storePhotos)){
                for (StorePhoto storePhoto : storePhotos) {
                    storePhoto.setSiId(storeInfo.getId());
                    storePhotoMapper.updateByPrimaryKey(storePhoto);
                }
            }

            CommonResult.success(jsonResult);
        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult uploadFile(Integer random, MultipartFile file, JsonResult jsonResult) {
        try {
            StorePhotoExample storePhotoExample = new StorePhotoExample();
            StorePhotoExample.Criteria criteria = storePhotoExample.or();
            criteria.andSiIdEqualTo(random);

            long num = storePhotoMapper.countByExample(storePhotoExample);

            if(num >= 5){
                jsonResult.setMessage("最多上传5张图片！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }


            if(file == null || file.isEmpty()){
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }

            String _key = storageService.uploadToPublicBucket("store", file);
            if (StringUtils.isEmpty(_key)) {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }

            StorePhoto storePhoto = new StorePhoto();
            storePhoto.setImgKey(_key);
            storePhoto.setCreateTime(new Timestamp(System.currentTimeMillis()));
            storePhoto.setSiId(random);
            storePhoto.setSort(99);
            storePhoto.setImgTitle("pic");
            storePhoto.setType(1);
            storePhotoMapper.insert(storePhoto);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.uploadFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectStoreInfoById(Integer storeId, JsonResult jsonResult) {
        try {

            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(storeId);

            if(ValidCheck.validPojo(storeInfo)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            if(storeInfo.getStatus() == DBConstants.MerchantStatus.DELETED.getCode()){
                jsonResult.setMessage("商户已删除！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }

            Map map = new HashMap();
            map.put("data", storeInfo);

            StorePhotoExample storePhotoExample = new StorePhotoExample();
            StorePhotoExample.Criteria criteria = storePhotoExample.or();
            criteria.andSiIdEqualTo(storeId);

            List<StorePhoto> storePhotos = storePhotoMapper.selectByExample(storePhotoExample);
            map.put("photos", storePhotos);

            jsonResult.setItem(map);

            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectStoreInfoByKeyword(String keyword, Integer page, Integer per_page, Integer sort, JsonResult jsonResult) {
        try {
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            StoreInfoExample.Criteria criteria = storeInfoExample.or();
            criteria.andStatusEqualTo(DBConstants.MerchantStatus.ACTIVED.getCode());
            criteria.andNameLike("%" + keyword + "%");

            PageHelper.startPage(page,per_page);
            List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);
            if(ValidCheck.validList(storeInfos)){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("没有找到商铺信息，请更换关键字！");
                return jsonResult;
            }

            PageInfo pageInfo = new PageInfo(storeInfos);
            Map map = new HashMap();
            map.put("data", storeInfos);

            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectStoreInfoByCode(Integer code, Integer page, Integer per_page, Integer sort, JsonResult jsonResult) {

        try{
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

            PageHelper.startPage(page,per_page);
            //查询分类下的商品，按照sort排序
            List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);

            PageInfo pageInfo = new PageInfo(storeInfos);
            Map  map = new HashMap();
            map.put("data", storeInfos);
            //设置总页面
            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());


            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public List<StoreInfo> selectStoreInfoByAid(Integer id) {
        StoreInfoExample storeInfoExample = new StoreInfoExample();
        StoreInfoExample.Criteria criteria = storeInfoExample.or();
        criteria.andAidEqualTo(id);
        return storeInfoMapper.selectByExample(storeInfoExample);
    }

    @Override
    public List<StorePhoto> selectStorePhotoById(Integer id) {
        StorePhotoExample storePhotoExample = new StorePhotoExample();
        StorePhotoExample.Criteria criteria = storePhotoExample.or();
        criteria.andSiIdEqualTo(id);
        return storePhotoMapper.selectByExample(storePhotoExample);
    }

    @Override
    public StoreExtInfo selectStoreExtInfoBySiId(Integer siId) {
        StoreExtInfoExample storeExtInfoExample = new StoreExtInfoExample();
        StoreExtInfoExample.Criteria criteria = storeExtInfoExample.or();
        criteria.andSiIdEqualTo(siId);
        List<StoreExtInfo> storeExtInfoList = storeExtInfoMapper.selectByExample(storeExtInfoExample);
        if(!storeExtInfoList.isEmpty()){
            return storeExtInfoList.get(0);
        }
        return null;
    }
}
