package com.gljr.jifen.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.ModuleAggregationService;
import com.gljr.jifen.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.GarbageCollectorMXBean;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class ModuleAggregationServiceImpl extends BaseService implements ModuleAggregationService {


    @Autowired
    private ModuleAggregationMapper moduleAggregationMapper;

    @Autowired
    private ModuleAggregationProductMapper moduleAggregationProductMapper;

    @Autowired
    private ModuleAggregationConditionMapper moduleAggregationConditionMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private FeaturedActivityMapper featuredActivityMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public JsonResult insertModuleAggregation(ModuleAggregation moduleAggregation, JsonResult jsonResult) {
        try{

            moduleAggregationMapper.insert(moduleAggregation);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult insertModuleAggregationProduct(Integer moduleAggregationId, String[] productIds, Integer type, JsonResult jsonResult) {

        try {
            for (String productId : productIds) {
                ModuleAggregationProduct moduleAggregationProduct = new ModuleAggregationProduct();
                if (type == 1) {

                    //判断聚合页内是否包含该商品，如果包含就不再次写入了
                    ModuleAggregationProductExample moduleAggregationProductExample = new ModuleAggregationProductExample();
                    ModuleAggregationProductExample.Criteria criteria = moduleAggregationProductExample.or();
                    criteria.andAggregationIdEqualTo(moduleAggregationId);
                    criteria.andProductIdEqualTo(Integer.parseInt(productId));


                    List<ModuleAggregationProduct> moduleAggregationProducts = moduleAggregationProductMapper.selectByExample(moduleAggregationProductExample);
                    if (ValidCheck.validList(moduleAggregationProducts)) {

                        moduleAggregationProduct.setAggregationId(moduleAggregationId);
                        moduleAggregationProduct.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        moduleAggregationProduct.setProductId(Integer.parseInt(productId));
                        moduleAggregationProduct.setType(type);
                        moduleAggregationProduct.setSort(9999);

                        moduleAggregationProductMapper.insert(moduleAggregationProduct);
                    }
                } else if (type == 2) {

                    ModuleAggregationProductExample moduleAggregationProductExample = new ModuleAggregationProductExample();
                    ModuleAggregationProductExample.Criteria criteria = moduleAggregationProductExample.or();
                    criteria.andAggregationIdEqualTo(moduleAggregationId);
                    criteria.andStoreIdEqualTo(Integer.parseInt(productId));

                    List<ModuleAggregationProduct> moduleAggregationProducts = moduleAggregationProductMapper.selectByExample(moduleAggregationProductExample);
                    if (ValidCheck.validList(moduleAggregationProducts)) {

                        moduleAggregationProduct.setAggregationId(moduleAggregationId);
                        moduleAggregationProduct.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        moduleAggregationProduct.setStoreId(Integer.parseInt(productId));
                        moduleAggregationProduct.setType(type);
                        moduleAggregationProduct.setSort(9999);

                        moduleAggregationProductMapper.insert(moduleAggregationProduct);
                    }
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
    public JsonResult selectModuleAggregations(Integer page, Integer per_page, JsonResult jsonResult) {
        try{
            ModuleAggregationExample moduleAggregationExample = new ModuleAggregationExample();
            ModuleAggregationExample.Criteria criteria = moduleAggregationExample.or();
            criteria.andStatusNotEqualTo(DBConstants.ModuleAggregationStatus.DELETED.getCode());
            moduleAggregationExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<ModuleAggregation> moduleAggregations = moduleAggregationMapper.selectByExample(moduleAggregationExample);
            PageInfo pageInfo = new PageInfo(moduleAggregations);

            if(!ValidCheck.validList(moduleAggregations)) {

                for (ModuleAggregation moduleAggregation : moduleAggregations) {
                    Admin admin = adminMapper.selectByPrimaryKey(moduleAggregation.getManagerId());

                    moduleAggregation.setAdmin(admin.getUsername());
                }
            }

            Map map = new HashMap();
            map.put("data", moduleAggregations);

            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            CommonResult.success(jsonResult);
            jsonResult.setItem(map);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectModuleAggregationById(Integer moduleAggregationId, JsonResult jsonResult) {
        try {
            ModuleAggregation moduleAggregation = moduleAggregationMapper.selectByPrimaryKey(moduleAggregationId);
            if(ValidCheck.validPojo(moduleAggregation)) {
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            Map map = new HashMap();
            map.put("data", moduleAggregation);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectModuleAggregationByEnabled(JsonResult jsonResult) {

        try{
            ModuleAggregationExample moduleAggregationExample = new ModuleAggregationExample();
            ModuleAggregationExample.Criteria criteria = moduleAggregationExample.or();
            criteria.andStatusEqualTo(DBConstants.ModuleAggregationStatus.ACTIVED.getCode());
            moduleAggregationExample.setOrderByClause("id desc");
            List<ModuleAggregation> moduleAggregations = moduleAggregationMapper.selectByExample(moduleAggregationExample);

            Map map = new HashMap();
            map.put("data", moduleAggregations);

            CommonResult.success(jsonResult);
            jsonResult.setItem(map);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult restartmoduleAggregationByLink(String link, JsonResult jsonResult) {
        ModuleAggregationExample moduleAggregationExample = new ModuleAggregationExample();
        ModuleAggregationExample.Criteria criteria = moduleAggregationExample.or();
        criteria.andStatusEqualTo(DBConstants.ModuleAggregationStatus.ACTIVED.getCode());
        criteria.andLinkCodeEqualTo(link);

        List<ModuleAggregation> moduleAggregations = moduleAggregationMapper.selectByExample(moduleAggregationExample);

        if(ValidCheck.validList(moduleAggregations)){
            CommonResult.noObject(jsonResult);
            return  jsonResult;
        }

        jsonResult = acceptanceModuleAggregationById(moduleAggregations.get(0).getId(), jsonResult);

        return jsonResult;
    }

    @Override
    public JsonResult rejectionModuleAggregationById(Integer moduleAggregationId, JsonResult jsonResult) {

        try {
            ModuleAggregation moduleAggregation = moduleAggregationMapper.selectByPrimaryKey(moduleAggregationId);

            //不存在
            if(ValidCheck.validPojo(moduleAggregation)) {
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

//            //精选页有没有用到聚合页
//            FeaturedActivityExample featuredActivityExample = new FeaturedActivityExample();
//            FeaturedActivityExample.Criteria criteria = featuredActivityExample.or();
//            criteria.andStatusNotEqualTo(DBConstants.FeaturedActivityStatus.DELETED.getCode());
//            criteria.andLinkUrlLike("%" + moduleAggregation.getLinkCode() + "%");
//
//            List<FeaturedActivity> featuredActivities = featuredActivityMapper.selectByExample(featuredActivityExample);
//
//            if(!ValidCheck.validList(featuredActivities)){
//                CommonResult.objIsUsed(jsonResult);
//                return jsonResult;
//            }

            moduleAggregation.setStatus(DBConstants.ModuleAggregationStatus.INACTIVE.getCode());

            //删除缓存
//            this.redisService.evict(moduleAggregation.getLinkCode(), moduleAggregation.getLinkCode()+"1",
//                    moduleAggregation.getLinkCode()+"2", moduleAggregation.getLinkCode()+"3", moduleAggregation.getLinkCode()+"4");

            moduleAggregationMapper.updateByPrimaryKey(moduleAggregation);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    public JsonResult acceptanceModuleAggregationById(Integer moduleAggregationId, JsonResult jsonResult) {
        try {
            ModuleAggregation moduleAggregation = moduleAggregationMapper.selectByPrimaryKey(moduleAggregationId);

            //判断是否找到该聚合页
            if(ValidCheck.validPojo(moduleAggregation)) {
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            moduleAggregation.setStatus(DBConstants.ModuleAggregationStatus.ACTIVED.getCode());
            moduleAggregationMapper.updateByPrimaryKey(moduleAggregation);

//            //查找聚合页下包含哪些商品和商户
//            ModuleAggregationProductExample moduleAggregationProductExample = new ModuleAggregationProductExample();
//            ModuleAggregationProductExample.Criteria criteria = moduleAggregationProductExample.or();
//            criteria.andAggregationIdEqualTo(moduleAggregation.getId());
//            List<ModuleAggregationProduct> moduleAggregationProducts = moduleAggregationProductMapper.selectByExample(moduleAggregationProductExample);
//
//
//            //如果聚合页下没有添加商品或者商户，不能启用聚合页
////            if(ValidCheck.validList(moduleAggregationProducts)) {
////                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
////                jsonResult.setMessage("请为聚合页选择商品或者商户！");
////                return jsonResult;
////
////            }
//
//            //循环获取商户或商品信息
//            List<AggregationProduct> aggregationProducts = new ArrayList<>();
//            for (ModuleAggregationProduct moduleAggregationProduct : moduleAggregationProducts) {
//                AggregationProduct aggregationProduct = new AggregationProduct();
//                if (moduleAggregationProduct.getType() == 1) {
//                    //线上商品
//                    Product product = productMapper.selectByPrimaryKey(moduleAggregationProduct.getProductId());
//                    if (!ValidCheck.validPojo(product)) {
//
//                        aggregationProduct.setId(product.getId());
//                        aggregationProduct.setIntegral(product.getIntegral());
//                        aggregationProduct.setName(product.getName());
//                        aggregationProduct.setPrice(product.getPrice());
//                        aggregationProduct.setType(DBConstants.CategoryType.PRODUCT.getCode());
//                        aggregationProduct.setLogoKey(product.getLogoKey() + "!popular");
//                        aggregationProduct.setSales(product.getSales());
//
//                        aggregationProducts.add(aggregationProduct);
//                    }
//                } else {
//                    //店铺
//                    StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(moduleAggregationProduct.getStoreId());
//                    if (!ValidCheck.validPojo(storeInfo)) {
//                        aggregationProduct.setLogoKey(storeInfo.getLogoKey() + "!popular");
//                        aggregationProduct.setType(DBConstants.CategoryType.STORE.getCode());
//                        aggregationProduct.setName(storeInfo.getName());
//                        aggregationProduct.setId(storeInfo.getId());
//                        aggregationProduct.setAddress(storeInfo.getAddress());
//
//                        aggregationProducts.add(aggregationProduct);
//                    }
//                }
//            }
//
//
//            //生成5种排序规则
//
//
//            if(moduleAggregation.getType() == DBConstants.ModuleAggregationType.PRODUCT.getCode()) {
//                //默认排序
//                this.redisService.put(moduleAggregation.getLinkCode(), JsonUtil.toJson(aggregationProducts), 60*60*24*30, TimeUnit.SECONDS);
//
//
//                //积分低到高
//                Collections.sort(aggregationProducts, new Comparator<AggregationProduct>() {
//
//                    @Override
//                    public int compare(AggregationProduct o1, AggregationProduct o2) {
//                        //按照学生的年龄进行升序排列
//                        if (o1.getIntegral() > o2.getIntegral()) {
//                            return 1;
//                        }
//                        if (o1.getIntegral() == o2.getIntegral()) {
//                            return 0;
//                        }
//                        return -1;
//                    }
//
//                });
//                this.redisService.put(moduleAggregation.getLinkCode() + "4", JsonUtil.toJson(aggregationProducts), 60*60*24*30, TimeUnit.SECONDS);
//                System.out.println(this.redisService.get(moduleAggregation.getLinkCode() + "4"));
//
//
//                //积分高到低
//                Collections.sort(aggregationProducts, new Comparator<AggregationProduct>() {
//
//                    @Override
//                    public int compare(AggregationProduct o1, AggregationProduct o2) {
//                        //按照学生的年龄进行升序排列
//                        if (o1.getIntegral() > o2.getIntegral()) {
//                            return -1;
//                        }
//                        if (o1.getIntegral() == o2.getIntegral()) {
//                            return 0;
//                        }
//                        return 1;
//                    }
//
//                });
//                this.redisService.put(moduleAggregation.getLinkCode() + "3", JsonUtil.toJson(aggregationProducts), 60*60*24*30, TimeUnit.SECONDS);
//
//
//                //销量低到高
//                Collections.sort(aggregationProducts, new Comparator<AggregationProduct>() {
//
//                    @Override
//                    public int compare(AggregationProduct o1, AggregationProduct o2) {
//                        //按照学生的年龄进行升序排列
//                        if (o1.getSales() > o2.getSales()) {
//                            return 1;
//                        }
//                        if (o1.getSales() == o2.getSales()) {
//                            return 0;
//                        }
//                        return -1;
//                    }
//
//                });
//                this.redisService.put(moduleAggregation.getLinkCode() + "2", JsonUtil.toJson(aggregationProducts), 60*60*24*30, TimeUnit.SECONDS);
//
//
//                //销量高到低
//                Collections.sort(aggregationProducts, new Comparator<AggregationProduct>() {
//
//                    @Override
//                    public int compare(AggregationProduct o1, AggregationProduct o2) {
//                        //按照学生的年龄进行升序排列
//                        if (o1.getSales() > o2.getSales()) {
//                            return -1;
//                        }
//                        if (o1.getSales() == o2.getSales()) {
//                            return 0;
//                        }
//                        return 1;
//                    }
//
//                });
//                this.redisService.put(moduleAggregation.getLinkCode() + "1", JsonUtil.toJson(aggregationProducts), 60*60*24*30, TimeUnit.SECONDS);
//            }
//
//
//            //更新聚合页状态
//            if(moduleAggregation.getStatus() != DBConstants.ModuleAggregationStatus.ACTIVED.getCode()) {
//                moduleAggregation.setStatus(DBConstants.ModuleAggregationStatus.ACTIVED.getCode());
//                moduleAggregationMapper.updateByPrimaryKey(moduleAggregation);
//            }

            CommonResult.success(jsonResult);

        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }





    @Override
    public JsonResult showModuleAggregationByLink(Integer moduleAggregationId, JsonResult jsonResult) {
        try {
            ModuleAggregation moduleAggregation = moduleAggregationMapper.selectByPrimaryKey(moduleAggregationId);

            //判断是否找到该聚合页
            if(ValidCheck.validPojo(moduleAggregation)) {
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            //查找聚合页下包含哪些商品和商户
            ModuleAggregationProductExample moduleAggregationProductExample = new ModuleAggregationProductExample();
            ModuleAggregationProductExample.Criteria criteria = moduleAggregationProductExample.or();
            criteria.andAggregationIdEqualTo(moduleAggregation.getId());
            List<ModuleAggregationProduct> moduleAggregationProducts = moduleAggregationProductMapper.selectByExample(moduleAggregationProductExample);



            //循环获取商户或商品信息
            List<AggregationProduct> aggregationProducts = new ArrayList<>();
            for (ModuleAggregationProduct moduleAggregationProduct : moduleAggregationProducts) {
                AggregationProduct aggregationProduct = new AggregationProduct();
                if (moduleAggregationProduct.getType() == 1) {
                    //线上商品
                    Product product = productMapper.selectByPrimaryKey(moduleAggregationProduct.getProductId());
                    if (!ValidCheck.validPojo(product)) {

                        aggregationProduct.setId(product.getId());
                        aggregationProduct.setIntegral(product.getIntegral());
                        aggregationProduct.setName(product.getName());
                        aggregationProduct.setPrice(product.getPrice());
                        aggregationProduct.setType(DBConstants.CategoryType.PRODUCT.getCode());
                        aggregationProduct.setLogoKey(product.getLogoKey() + "!popular");
                        aggregationProduct.setSales(product.getSales());

                        aggregationProducts.add(aggregationProduct);
                    }
                } else {
                    //店铺
                    StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(moduleAggregationProduct.getStoreId());
                    if (!ValidCheck.validPojo(storeInfo)) {
                        aggregationProduct.setLogoKey(storeInfo.getLogoKey() + "!popular");
                        aggregationProduct.setType(DBConstants.CategoryType.STORE.getCode());
                        aggregationProduct.setName(storeInfo.getName());
                        aggregationProduct.setId(storeInfo.getId());
                        aggregationProduct.setAddress(storeInfo.getAddress());

                        aggregationProducts.add(aggregationProduct);
                    }
                }
            }

            Map map = new HashMap();
            map.put("data", aggregationProducts);

            jsonResult.setItem(map);

            jsonResult.setMessage(moduleAggregation.getTitle());
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);


        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult insertModuleAggregationCondition(Integer id, Integer from, Integer to) {
        try {
            String str = from + "," + to;

            ModuleAggregationConditionExample moduleAggregationConditionExample = new ModuleAggregationConditionExample();
            ModuleAggregationConditionExample.Criteria criteria = moduleAggregationConditionExample.or();
            criteria.andAggregationIdEqualTo(id);
            List<ModuleAggregationCondition> moduleAggregationConditions = moduleAggregationConditionMapper.selectByExample(moduleAggregationConditionExample);

            if(ValidCheck.validList(moduleAggregationConditions)){
                ModuleAggregationCondition moduleAggregationCondition = new ModuleAggregationCondition();
                moduleAggregationCondition.setAggregationId(id);
                moduleAggregationCondition.setCreateTime(new Timestamp(System.currentTimeMillis()));
                moduleAggregationCondition.setExpStr(str);
                moduleAggregationConditionMapper.insert(moduleAggregationCondition);
            }else {
                ModuleAggregationCondition moduleAggregationCondition = moduleAggregationConditions.get(0);
                moduleAggregationCondition.setExpStr(str);
                moduleAggregationConditionMapper.updateByPrimaryKeyWithBLOBs(moduleAggregationCondition);
            }

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult deleteModuleAggregationById(Integer id) {
        try {
            ModuleAggregation moduleAggregation = moduleAggregationMapper.selectByPrimaryKey(id);
            moduleAggregation.setStatus(DBConstants.ProductStatus.DELETED.getCode());

            moduleAggregationMapper.updateByPrimaryKey(moduleAggregation);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }
}
