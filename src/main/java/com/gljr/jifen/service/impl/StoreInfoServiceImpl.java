package com.gljr.jifen.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.*;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.*;
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
    private DTChainService chainService;

    @Autowired
    private StoreCouponMapper storeCouponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    public JsonResult selectAllStoreInfo(Integer page, Integer per_page, JsonResult jsonResult) {
        try {
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            StoreInfoExample.Criteria criteria = storeInfoExample.or();
            criteria.andStatusNotEqualTo(DBConstants.MerchantStatus.DELETED.getCode());
            criteria.andStoreTypeEqualTo(DBConstants.MerchantType.OFFLINE.getCode());
            storeInfoExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);
            PageInfo pageInfo = new PageInfo(storeInfos);

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
            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectAllOnlineStoreInfo(Integer page, Integer per_page,JsonResult jsonResult) {
        try {
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            StoreInfoExample.Criteria criteria = storeInfoExample.or();
            criteria.andStatusNotEqualTo(DBConstants.MerchantStatus.DELETED.getCode());
            criteria.andStoreTypeEqualTo(DBConstants.MerchantType.ONLINE.getCode());
            storeInfoExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);
            PageInfo pageInfo = new PageInfo(storeInfos);


            Map map = new HashMap();
            map.put("data", storeInfos);
            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

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
    public JsonResult insertStoreInfo(StoreInfo storeInfo, MultipartFile file, String username, Integer random, JsonResult jsonResult, String type) {
        try {

            //添加商户管理员账号
            AdminExample adminExample = new AdminExample();
            AdminExample.Criteria criteria = adminExample.or();
            criteria.andUsernameEqualTo(username);

            System.out.println(storeInfo.getStoreType());

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
            storeInfo.setSerialCode(serialNumberService.gextNextStoreSerialCode(storeInfo.getLocationCode()));
            storeInfo.setAid(admin.getId());
            storeInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));

            String[] types = type.split(",");

            for(String _type : types){
                if(_type.equals("1")){
                    storeInfo.setCategoryCode(-1);
                    storeInfo.setStoreType(DBConstants.MerchantType.ONLINE.getCode());
                    storeInfoMapper.insert(storeInfo);
                }
                if(_type.equals("2")){
                    storeInfo.setStoreType(DBConstants.MerchantType.OFFLINE.getCode());
                    storeInfoMapper.insert(storeInfo);
                }
            }

            //添加商户的积分信息
            GatewayResponse response = this.chainService.initStoreAccount(storeInfo.getId() + 0L);

            if (null == response || response.getCode() != 200) {
                throw new Exception("数链网络内创建商户账号失败");
            }

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
            System.out.println(e);
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

            StoreCouponExample storeCouponExample = new StoreCouponExample();
            StoreCouponExample.Criteria criteria1 = storeCouponExample.or();
            criteria1.andSiIdEqualTo(storeId);
            criteria1.andStatusEqualTo(1);
            List<StoreCoupon> storeCoupons = storeCouponMapper.selectByExample(storeCouponExample);

            for (StoreCoupon storeCoupon : storeCoupons){
                UserCouponExample userCouponExample = new UserCouponExample();
                UserCouponExample.Criteria criteria2 = userCouponExample.or();
                criteria2.andStatusNotEqualTo(DBConstants.CouponStatus.REFUND.getCode());
                criteria2.andScIdEqualTo(storeCoupon.getId());

                Long num = userCouponMapper.countByExample(userCouponExample);

                storeCoupon.setRemainingAmount(storeCoupon.getMaxGenerated() - Integer.parseInt(num + ""));
            }

            map.put("coupons",storeCoupons);


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
            criteria.andStoreTypeEqualTo(DBConstants.MerchantType.ONLINE.getCode());
            criteria.andNameLike("%" + keyword + "%");

            PageHelper.startPage(page,per_page);
            List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);

            for (StoreInfo storeInfo : storeInfos){
                storeInfo.setLogoKey(storeInfo.getLogoKey() + "!popular");
            }

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
    public JsonResult selectOfflineStoreInfoByKeyword(String keyword, Integer page, Integer per_page, Integer sort, JsonResult jsonResult) {
        try {
            StoreInfoExample storeInfoExample = new StoreInfoExample();
            StoreInfoExample.Criteria criteria = storeInfoExample.or();
            criteria.andStatusEqualTo(DBConstants.MerchantStatus.ACTIVED.getCode());
            criteria.andStoreTypeEqualTo(DBConstants.MerchantType.OFFLINE.getCode());
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
                storeInfoExample.setOrderByClause("pay_status desc");
            }else if (sort == 2){
                storeInfoExample.setOrderByClause("pay_status asc");
            }

            PageHelper.startPage(page,per_page);
            //查询分类下的商品，按照sort排序
            List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);

            for (StoreInfo storeInfo : storeInfos){
                storeInfo.setLogoKey(storeInfo.getLogoKey() + "!popular");
            }

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
            System.out.println(e);
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
    public StoreInfo selectStoreInfoById(Integer siid) {
        return storeInfoMapper.selectByPrimaryKey(siid);
    }

    @Override
    public JsonResult allowCancelStoreCouponById(Integer id, Integer allow) {
        JsonResult jsonResult = new JsonResult();
        try{
            StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(id);
            storeInfo.setPayStatus(allow);
            storeInfoMapper.updateByPrimaryKey(storeInfo);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult insertStoreExtInfo(StoreExtInfo storeExtInfo, MultipartFile file, JsonResult jsonResult) {

        try {



            StoreExtInfoExample storeExtInfoExample = new StoreExtInfoExample();
            StoreExtInfoExample.Criteria criteria = storeExtInfoExample.or();
            criteria.andSiIdEqualTo(storeExtInfo.getSiId());

            List<StoreExtInfo> storeExtInfos = storeExtInfoMapper.selectByExample(storeExtInfoExample);
            if(ValidCheck.validList(storeExtInfos)){
                storeExtInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));

                if (file != null && !file.isEmpty()) {
                    String _key = storageService.uploadToPublicBucket("store", file);
                    if (StringUtils.isEmpty(_key)) {
                        CommonResult.uploadFailed(jsonResult);
                        return jsonResult;
                    }
                    storeExtInfo.setLicenseFileKey(_key);
                } else {
                    jsonResult.setMessage("请上传营业执照");
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    return jsonResult;
                }

                storeExtInfoMapper.insert(storeExtInfo);
            }else {
                storeExtInfo.setId(storeExtInfos.get(0).getId());
                storeExtInfo.setCreateTime(storeExtInfos.get(0).getCreateTime());

                if (file != null && !file.isEmpty()) {
                    String _key = storageService.uploadToPublicBucket("store", file);
                    if (StringUtils.isEmpty(_key)) {
                        CommonResult.uploadFailed(jsonResult);
                        return jsonResult;
                    }
                    storeExtInfo.setLicenseFileKey(_key);
                } else {
                    storeExtInfo.setLicenseFileKey(storeExtInfos.get(0).getLicenseFileKey());
                }

                storeExtInfoMapper.updateByPrimaryKey(storeExtInfo);
            }

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    public JsonResult selectStoreExtInfoById(Integer id, JsonResult jsonResult) {
        try {
            StoreExtInfoExample storeExtInfoExample = new StoreExtInfoExample();
            StoreExtInfoExample.Criteria criteria = storeExtInfoExample.or();
            criteria.andSiIdEqualTo(id);
            List<StoreExtInfo> storeExtInfos = storeExtInfoMapper.selectByExample(storeExtInfoExample);
            if(ValidCheck.validList(storeExtInfos)){
                jsonResult.setItem(null);
            }else {
                Map map = new HashMap();
                map.put("data", storeExtInfos.get(0));
                jsonResult.setItem(map);
            }
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
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
