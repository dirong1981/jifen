package com.gljr.jifen.service.impl;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.AdminMapper;
import com.gljr.jifen.dao.ModuleAggregationMapper;
import com.gljr.jifen.dao.ModuleAggregationProductMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.ModuleAggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.GarbageCollectorMXBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ModuleAggregationServiceImpl implements ModuleAggregationService {


    @Autowired
    private ModuleAggregationMapper moduleAggregationMapper;

    @Autowired
    private ModuleAggregationProductMapper moduleAggregationProductMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public JsonResult insertModuleAggregation(ModuleAggregation moduleAggregation, JsonResult jsonResult) {
        try{
            Admin admin = adminMapper.selectByPrimaryKey(moduleAggregation.getManagerId());
            if(ValidCheck.validPojo(admin)){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }

            if(admin.getAccountType() != DBConstants.AdminAccountType.SYS_ADMIN.getCode()){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("没有权限！");
                return jsonResult;
            }

            moduleAggregationMapper.insert(moduleAggregation);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public int insertModuleAggregationProduct(Integer id, String[] productIds, Integer type) {
        for (String productId : productIds){
            ModuleAggregationProduct moduleAggregationProduct = new ModuleAggregationProduct();
            if(type == 1) {

                //判断聚合页内是否包含该商品，如果包含就不再次写入了
                ModuleAggregationProductExample moduleAggregationProductExample = new ModuleAggregationProductExample();
                ModuleAggregationProductExample.Criteria criteria = moduleAggregationProductExample.or();
                criteria.andAggregationIdEqualTo(id);
                criteria.andProductIdEqualTo(Integer.parseInt(productId));


                List<ModuleAggregationProduct> moduleAggregationProducts = moduleAggregationProductMapper.selectByExample(moduleAggregationProductExample);
                if(ValidCheck.validList(moduleAggregationProducts)) {

                    moduleAggregationProduct.setAggregationId(id);
                    moduleAggregationProduct.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    moduleAggregationProduct.setProductId(Integer.parseInt(productId));
                    moduleAggregationProduct.setType(type);
                    moduleAggregationProduct.setSort(9999);

                    moduleAggregationProductMapper.insert(moduleAggregationProduct);
                }
            }else if (type == 2){

                ModuleAggregationProductExample moduleAggregationProductExample = new ModuleAggregationProductExample();
                ModuleAggregationProductExample.Criteria criteria = moduleAggregationProductExample.or();
                criteria.andAggregationIdEqualTo(id);
                criteria.andStoreIdEqualTo(Integer.parseInt(productId));

                List<ModuleAggregationProduct> moduleAggregationProducts = moduleAggregationProductMapper.selectByExample(moduleAggregationProductExample);
                if(ValidCheck.validList(moduleAggregationProducts)) {

                    moduleAggregationProduct.setAggregationId(id);
                    moduleAggregationProduct.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    moduleAggregationProduct.setStoreId(Integer.parseInt(productId));
                    moduleAggregationProduct.setType(type);
                    moduleAggregationProduct.setSort(9999);

                    moduleAggregationProductMapper.insert(moduleAggregationProduct);
                }
            }


        }
        return 0;
    }

    @Override
    public JsonResult selectModuleAggregations(JsonResult jsonResult) {
        try{
            ModuleAggregationExample moduleAggregationExample = new ModuleAggregationExample();
            moduleAggregationExample.setOrderByClause("id desc");

            List<ModuleAggregation> moduleAggregations = moduleAggregationMapper.selectByExample(moduleAggregationExample);

            if(!ValidCheck.validList(moduleAggregations)) {

                for (ModuleAggregation moduleAggregation : moduleAggregations) {
                    Admin admin = adminMapper.selectByPrimaryKey(moduleAggregation.getManagerId());

                    moduleAggregation.setAdmin(admin.getUsername());
                }
            }

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
    public ModuleAggregation selectModuleAggregationById(Integer id) {
        return moduleAggregationMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ModuleAggregationProduct> selectModuleAggregationProductByAggregationId(Integer aid) {
        ModuleAggregationProductExample moduleAggregationProductExample = new ModuleAggregationProductExample();
        ModuleAggregationProductExample.Criteria criteria = moduleAggregationProductExample.or();
        criteria.andAggregationIdEqualTo(aid);
        return moduleAggregationProductMapper.selectByExample(moduleAggregationProductExample);
    }

    @Override
    public JsonResult selectModuleAggregationByEnabled(JsonResult jsonResult) {

        try{
            ModuleAggregationExample moduleAggregationExample = new ModuleAggregationExample();
            ModuleAggregationExample.Criteria criteria = moduleAggregationExample.or();
            criteria.andStatusEqualTo(DBConstants.CategoryStatus.ACTIVED.getCode());
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
    public int updateModuleAggregationById(ModuleAggregation moduleAggregation) {
        return moduleAggregationMapper.updateByPrimaryKey(moduleAggregation);
    }
}
