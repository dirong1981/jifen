package com.gljr.jifen.service.impl;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.PlateService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlateServiceImpl implements PlateService {

    @Autowired
    private PlateMapper plateMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private ModulePictureMapper modulePictureMapper;

    @Autowired
    private ModuleProductMapper moduleProductMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public JsonResult selectPlates(JsonResult jsonResult) {
        try {
            PlateExample plateExample = new PlateExample();
            plateExample.setOrderByClause("sort asc");

            List<Plate> plates = plateMapper.selectByExample(plateExample);

            if(!ValidCheck.validList(plates)){
                for (Plate plate : plates){
                    Module module = moduleMapper.selectByPrimaryKey(plate.getModuleId());
                    plate.setDescription(module.getDescription());
                    plate.setTitle(module.getTitle());
                    plate.setType(module.getType());
                    plate.setExtType(module.getExtType());
                }
            }
            Map map = new HashMap();
            map.put("data", plates);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    public JsonResult generatePlates(JsonResult jsonResult) {
        try {
            PlateExample plateExample = new PlateExample();
            PlateExample.Criteria criteria = plateExample.or();
            plateExample.setOrderByClause("sort asc");
            List<Plate> plates = plateMapper.selectByExample(plateExample);

            Map map = new HashMap();

            List list = new ArrayList();

            JSONObject jsonObject = new JSONObject();

            for (Plate plate : plates){

                Module module = moduleMapper.selectByPrimaryKey(plate.getModuleId());
                if(module.getStatus() == DBConstants.ModuleStatus.ACTIVED.getCode()) {
//                    jsonObject.put("title", module.getTitle());
//                    jsonObject.put("description", module.getDescription());
//                    jsonObject.put("type", module.getType());
//                    jsonObject.put()



                    if(module.getType() == DBConstants.ModuleType.PICTURE.getCode()){
                        ModulePictureExample modulePictureExample = new ModulePictureExample();
                        ModulePictureExample.Criteria criteria1 = modulePictureExample.or();
                        criteria1.andModuleIdEqualTo(module.getId());
                        modulePictureExample.setOrderByClause("id asc");

                        List<ModulePicture> modulePictures = modulePictureMapper.selectByExample(modulePictureExample);
                        module.setPicture(modulePictures);
                    }

                    if(module.getType() == DBConstants.ModuleType.PRODUCT.getCode()){
                        ModuleProductExample moduleProductExample = new ModuleProductExample();
                        ModuleProductExample.Criteria criteria1 = moduleProductExample.or();
                        criteria1.andModuleIdEqualTo(module.getId());
                        moduleProductExample.setOrderByClause("id asc");

                        List<ModuleProduct> moduleProducts = moduleProductMapper.selectByExample(moduleProductExample);
                        for (ModuleProduct moduleProduct : moduleProducts){
                            Product product = productMapper.selectByPrimaryKey(moduleProduct.getProductId());
                            if(!ValidCheck.validPojo(product)){
                                moduleProduct.setLogoKey(product.getLogoKey());
                            }
                        }
                        module.setProduct(moduleProducts);
                    }

                    if(module.getType() == DBConstants.ModuleType.PICTUREANDPRODUCT.getCode()){
                        ModulePictureExample modulePictureExample = new ModulePictureExample();
                        ModulePictureExample.Criteria criteria1 = modulePictureExample.or();
                        criteria1.andModuleIdEqualTo(module.getId());

                        List<ModulePicture> modulePictures = modulePictureMapper.selectByExample(modulePictureExample);
                        module.setPicture(modulePictures);

                        ModuleProductExample moduleProductExample = new ModuleProductExample();
                        ModuleProductExample.Criteria criteria2 = moduleProductExample.or();
                        criteria2.andModuleIdEqualTo(module.getId());
                        List<ModuleProduct> moduleProducts = moduleProductMapper.selectByExample(moduleProductExample);
                        for (ModuleProduct moduleProduct : moduleProducts){
                            Product product = productMapper.selectByPrimaryKey(moduleProduct.getProductId());
                            if(!ValidCheck.validPojo(product)){
                                moduleProduct.setLogoKey(product.getLogoKey());
                            }
                        }
                        module.setProduct(moduleProducts);
                    }

                    list.add(module);
                }
            }

            map.put("data", list);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult changePlateOrder(Integer cur, Integer prev, JsonResult jsonResult) {

        try {
            Plate plate = plateMapper.selectByPrimaryKey(cur);
            Plate plate1 = plateMapper.selectByPrimaryKey(prev);

            cur = plate.getSort();
            prev = plate1.getSort();

            plate.setSort(prev);
            plate1.setSort(cur);

            plateMapper.updateByPrimaryKey(plate);
            plateMapper.updateByPrimaryKey(plate1);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }
}
