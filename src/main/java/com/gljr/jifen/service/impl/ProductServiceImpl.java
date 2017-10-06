package com.gljr.jifen.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.ProductService;
import com.gljr.jifen.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductPhotoMapper productPhotoMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private ModuleAggregationProductMapper moduleAggregationProductMapper;

    @Autowired
    private ModuleProductMapper moduleProductMapper;

    @Autowired
    private ModuleAggregationMapper moduleAggregationMapper;

    @Autowired
    private StorageService storageService;

    @Autowired
    private OnlineOrderMapper onlineOrderMapper;


    @Override
    public JsonResult selectAllProduct(JsonResult jsonResult) {

        try{
            ProductExample productExample = new ProductExample();
            ProductExample.Criteria criteria = productExample.or();
            criteria.andStatusNotEqualTo(DBConstants.ProductStatus.DELETED.getCode());
            productExample.setOrderByClause("id desc");
            List<Product> products = productMapper.selectByExample(productExample);
            //获取商户名称
            if(!ValidCheck.validList(products)){
                for (Product product : products){
                    StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(product.getSiId());
                    if(ValidCheck.validPojo(storeInfo)){
                        product.setStoreName("已删除");
                    }else {
                        product.setStoreName(storeInfo.getName());
                    }
                }
            }

            Map map = new HashMap();
            map.put("data", products);


            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult stopProductById(Integer productId, JsonResult jsonResult) {
        try {
            Product product = productMapper.selectByPrimaryKey(productId);
            if(ValidCheck.validPojo(product)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

//            //判断聚合页是否包含该商品
//            ModuleAggregationProductExample moduleAggregationProductExample = new ModuleAggregationProductExample();
//            ModuleAggregationProductExample.Criteria criteria = moduleAggregationProductExample.or();
//            criteria.andProductIdEqualTo(productId);
//            List<ModuleAggregationProduct> moduleAggregationProducts = moduleAggregationProductMapper.selectByExample(moduleAggregationProductExample);
//
//            if(!ValidCheck.validList(moduleAggregationProducts)){
//                //判断聚合页是否在使用,多个聚合页
//                for (ModuleAggregationProduct moduleAggregationProduct : moduleAggregationProducts) {
//                    ModuleAggregation moduleAggregation = moduleAggregationMapper.selectByPrimaryKey(moduleAggregationProduct.getAggregationId());
//                    if (!ValidCheck.validPojo(moduleAggregation)) {
//                        if (moduleAggregation.getStatus() == DBConstants.ModuleAggregationStatus.ACTIVED.getCode()) {
//                            CommonResult.objIsUsed(jsonResult);
//                            return jsonResult;
//                        }
//                    }
//                }
//            }
//
//            //判断模块页是否包含该商品
//            ModuleProductExample moduleProductExample = new ModuleProductExample();
//            ModuleProductExample.Criteria criteria1 = moduleProductExample.or();
//            criteria1.andProductIdEqualTo(productId);
//            List<ModuleProduct> moduleProducts = moduleProductMapper.selectByExample(moduleProductExample);
//
//            //模块页是否是激活状态
//            if(!ValidCheck.validList(moduleProducts)){
//                for (ModuleProduct moduleProduct : moduleProducts){
//
//                }
//            }

            product.setStatus(DBConstants.ProductStatus.OFF_SALE.getCode());

            productMapper.updateByPrimaryKey(product);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult startProductById(Integer productId, JsonResult jsonResult) {
        try {
            Product product = productMapper.selectByPrimaryKey(productId);
            if(ValidCheck.validPojo(product)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }
            product.setStatus(DBConstants.ProductStatus.ON_SALE.getCode());

            productMapper.updateByPrimaryKey(product);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult deleteProductById(Integer productId, JsonResult jsonResult) {
        try {
            Product product = productMapper.selectByPrimaryKey(productId);
            if(ValidCheck.validPojo(product)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

//            //判断聚合页是否包含该商品
//            ModuleAggregationProductExample moduleAggregationProductExample = new ModuleAggregationProductExample();
//            ModuleAggregationProductExample.Criteria criteria = moduleAggregationProductExample.or();
//            criteria.andProductIdEqualTo(productId);
//            List<ModuleAggregationProduct> moduleAggregationProducts = moduleAggregationProductMapper.selectByExample(moduleAggregationProductExample);
//
//            if(!ValidCheck.validList(moduleAggregationProducts)){
//                CommonResult.objIsUsed(jsonResult);
//                return jsonResult;
//            }
//
//            //判断模块页是否包含该商品
//            ModuleProductExample moduleProductExample = new ModuleProductExample();
//            ModuleProductExample.Criteria criteria1 = moduleProductExample.or();
//            criteria1.andProductIdEqualTo(productId);
//            List<ModuleProduct> moduleProducts = moduleProductMapper.selectByExample(moduleProductExample);
//
//            if(!ValidCheck.validList(moduleProducts)){
//                CommonResult.objIsUsed(jsonResult);
//                return jsonResult;
//            }

            product.setStatus(DBConstants.ProductStatus.DELETED.getCode());
            productMapper.updateByPrimaryKey(product);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult insertProduct(Product product, MultipartFile file, Integer random, JsonResult jsonResult) {
        //上传图片

        if (file != null && !file.isEmpty()) {
            String _key = storageService.uploadToPublicBucket("product", file);
            if (StringUtils.isEmpty(_key)) {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }
            product.setLogoKey(_key);
        } else {
            product.setLogoKey("product/default.png");
        }


        try {

            product.setStatus(DBConstants.ProductStatus.OFF_SALE.getCode());
            product.setCreateTime(new Timestamp(System.currentTimeMillis()));

            productMapper.insert(product);

            //更新商品图片记录中的商品id
            ProductPhotoExample productPhotoExample = new ProductPhotoExample();
            ProductPhotoExample.Criteria criteria = productPhotoExample.or();
            criteria.andPidEqualTo(random);

            List<ProductPhoto> productPhotos = productPhotoMapper.selectByExample(productPhotoExample);

            if(!ValidCheck.validList(productPhotos)){
                for (ProductPhoto productPhoto : productPhotos){
                    productPhoto.setPid(product.getId());
                    productPhotoMapper.updateByPrimaryKey(productPhoto);
                }
            }

            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult uploadFile(MultipartFile file, Integer random, JsonResult jsonResult) {
        try {

            String _key = storageService.uploadToPublicBucket("product", file);
            if (StringUtils.isEmpty(_key)) {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }

            ProductPhoto productPhoto = new ProductPhoto();
            productPhoto.setImgKey(_key);
            productPhoto.setCreateTime(new Timestamp(System.currentTimeMillis()));
            productPhoto.setPid(random);
            productPhoto.setSort(99);

            productPhotoMapper.insert(productPhoto);


            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectProductById(Integer productId, String uid, JsonResult jsonResult) {

        try{
            Product product = productMapper.selectByPrimaryKey(productId);
            if(ValidCheck.validPojo(product)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            if(product.getStatus() == DBConstants.ProductStatus.DELETED.getCode()){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("商品已删除！");
                return jsonResult;
            }

            //查询用户已购买数量
            OnlineOrderExample onlineOrderExample = new OnlineOrderExample();
            OnlineOrderExample.Criteria criteria = onlineOrderExample.or();
            criteria.andUidEqualTo(Integer.parseInt(uid));
            criteria.andPidEqualTo(productId);

            List<OnlineOrder> onlineOrders = onlineOrderMapper.selectByExample(onlineOrderExample);

            int userPurchases = 0;

            if(ValidCheck.validList(onlineOrders)){
                for (OnlineOrder onlineOrder : onlineOrders){
                    userPurchases += onlineOrder.getQuantity();
                }
            }

            product.setUserPurchases(userPurchases);

            Map  map = new HashMap();
            map.put("data", product);

            //查找商品图片
            ProductPhotoExample productPhotoExample = new ProductPhotoExample();
            ProductPhotoExample.Criteria criteria1 = productPhotoExample.or();
            criteria1.andPidEqualTo(productId);
            List<ProductPhoto> productPhotos = productPhotoMapper.selectByExample(productPhotoExample);

            map.put("photos", productPhotos);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectProductByKeyword(String keyword, Integer page, Integer per_page, Integer sort, JsonResult jsonResult) {

        try{
            ProductExample productExample = new ProductExample();
            ProductExample.Criteria criteria = productExample.createCriteria();
            criteria.andStatusEqualTo(DBConstants.ProductStatus.ON_SALE.getCode());
            criteria.andNameLike("%" + keyword + "%");
            //设置排序
            if(sort == 0){
                productExample.setOrderByClause("id desc");
            }else if (sort == 1){
                productExample.setOrderByClause("sales desc, id desc");
            }else if (sort == 2){
                productExample.setOrderByClause("sales asc, id desc");
            }else if (sort == 3){
                productExample.setOrderByClause("integral desc, id desc");
            }else if (sort == 4){
                productExample.setOrderByClause("integral asc, id desc");
            }

            PageHelper.startPage(page,per_page);
            List<Product> list = productMapper.selectByExample(productExample);

            PageInfo pageInfo = new PageInfo(list);
            Map  map = new HashMap();
            map.put("data", list);

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
    public JsonResult selectProductByCode(Integer code, Integer page, Integer per_page, Integer sort, JsonResult jsonResult) {
        try{
            ProductExample productExample = new ProductExample();
            ProductExample.Criteria criteria = productExample.createCriteria();
            criteria.andStatusEqualTo(DBConstants.ProductStatus.ON_SALE.getCode());
            criteria.andCategoryCodeEqualTo(code);
            //设置排序
            if(sort == 0){
                productExample.setOrderByClause("id desc");
            }else if (sort == 1){
                productExample.setOrderByClause("sales desc, id desc");
            }else if (sort == 2){
                productExample.setOrderByClause("sales asc, id desc");
            }else if (sort == 3){
                productExample.setOrderByClause("integral desc, id desc");
            }else if (sort == 4){
                productExample.setOrderByClause("integral asc, id desc");
            }


            PageHelper.startPage(page,per_page);
            //查询分类下的商品，按照sort排序
            List<Product> list = productMapper.selectByExample(productExample);

            PageInfo pageInfo = new PageInfo(list);
            Map  map = new HashMap();
            map.put("data", list);
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

//
//
//    @Override
//    public List<Product> selectCategoryProduct(int code, int sort) {
//        ProductExample productExample = new ProductExample();
//        ProductExample.Criteria criteria = productExample.createCriteria();
//        criteria.andStatusEqualTo(DBConstants.ProductStatus.ON_SALE.getCode());
//        criteria.andCategoryCodeEqualTo(code);
//        //设置排序
//        if(sort == 0){
//            productExample.setOrderByClause("id desc");
//        }else if (sort == 1){
//            productExample.setOrderByClause("sales desc, id desc");
//        }else if (sort == 2){
//            productExample.setOrderByClause("sales asc, id desc");
//        }else if (sort == 3){
//            productExample.setOrderByClause("integral desc, id desc");
//        }else if (sort == 4){
//            productExample.setOrderByClause("integral asc, id desc");
//        }
//        return productMapper.selectByExample(productExample);
//    }


}
