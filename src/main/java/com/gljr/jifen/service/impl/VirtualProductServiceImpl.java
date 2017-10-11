package com.gljr.jifen.service.impl;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.dao.SystemVirtualProductMapper;
import com.gljr.jifen.dao.VirtualProductMapper;
import com.gljr.jifen.pojo.SystemVirtualProduct;
import com.gljr.jifen.pojo.SystemVirtualProductExample;
import com.gljr.jifen.pojo.VirtualProduct;
import com.gljr.jifen.pojo.VirtualProductExample;
import com.gljr.jifen.service.VirtualProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VirtualProductServiceImpl implements VirtualProductService {

    @Autowired
    private SystemVirtualProductMapper systemVirtualProductMapper;

    @Autowired
    private VirtualProductMapper virtualProductMapper;

    @Override
    public JsonResult selectEnabelSystemVirtualProduct(JsonResult jsonResult) {
        try {
            SystemVirtualProductExample systemVirtualProductExample = new SystemVirtualProductExample();
            SystemVirtualProductExample.Criteria criteria = systemVirtualProductExample.or();
            criteria.andStatusEqualTo(1);
            systemVirtualProductExample.setOrderByClause("id desc");

            List<SystemVirtualProduct> systemVirtualProducts = systemVirtualProductMapper.selectByExample(systemVirtualProductExample);

            Map map = new HashMap();
            map.put("data", systemVirtualProducts);
            CommonResult.success(jsonResult);
            jsonResult.setItem(map);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult insertVirtualProduct(VirtualProduct virtualProduct, JsonResult jsonResult) {
        try {
            virtualProductMapper.insert(virtualProduct);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectVirtualProduct(JsonResult jsonResult) {
        try {
            VirtualProductExample virtualProductExample = new VirtualProductExample();
            virtualProductExample.setOrderByClause("id desc");
            List<VirtualProduct> virtualProducts = virtualProductMapper.selectByExample(virtualProductExample);

            for(VirtualProduct virtualProduct : virtualProducts){
                SystemVirtualProduct systemVirtualProduct = systemVirtualProductMapper.selectByPrimaryKey(virtualProduct.getVpId());
                virtualProduct.setType(systemVirtualProduct.getType());
                virtualProduct.setIntegral(systemVirtualProduct.getIntegral());
            }

            Map map = new HashMap();
            map.put("data", virtualProducts);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult startVirtualProduct(Integer id, JsonResult jsonResult) {
        try{
            VirtualProduct virtualProduct = virtualProductMapper.selectByPrimaryKey(id);

            if(ValidCheck.validPojo(virtualProduct)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            virtualProduct.setStatus(1);

            virtualProductMapper.updateByPrimaryKey(virtualProduct);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult stopVirtualProduct(Integer id, JsonResult jsonResult) {
        try{
            VirtualProduct virtualProduct = virtualProductMapper.selectByPrimaryKey(id);

            if(ValidCheck.validPojo(virtualProduct)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            virtualProduct.setStatus(0);

            virtualProductMapper.updateByPrimaryKey(virtualProduct);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectEnableVirtualProduct(JsonResult jsonResult) {
        try {
            VirtualProductExample virtualProductExample = new VirtualProductExample();
            VirtualProductExample.Criteria criteria = virtualProductExample.or();
            criteria.andStatusEqualTo(1);
            virtualProductExample.setOrderByClause("id desc");

            List<VirtualProduct> virtualProducts = virtualProductMapper.selectByExample(virtualProductExample);

            Map map = new HashMap();
            map.put("data", virtualProducts);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectVirtualProductById(Integer virtualId, JsonResult jsonResult) {
        try {
            VirtualProduct virtualProduct = virtualProductMapper.selectByPrimaryKey(virtualId);
            SystemVirtualProduct systemVirtualProduct = systemVirtualProductMapper.selectByPrimaryKey(virtualProduct.getVpId());

            virtualProduct.setType(systemVirtualProduct.getType());
            virtualProduct.setIntegral(systemVirtualProduct.getIntegral());

            Map map = new HashMap();
            map.put("data", virtualProduct);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }
}
