package com.gljr.jifen.service.impl;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.ModuleService;
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
public class ModuleServiceImpl implements ModuleService {


    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private ModulePictureMapper modulePictureMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ModuleProductMapper moduleProductMapper;

    @Autowired
    private PlateMapper plateMapper;

    @Autowired
    private StorageService storageService;


    @Override
    public JsonResult insertModuel(Module module, JsonResult jsonResult) {

        try {

            moduleMapper.insert(module);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    public JsonResult uploadFile(MultipartFile file, ModulePicture modulePicture, JsonResult jsonResult) {
        try{
            Module module = moduleMapper.selectByPrimaryKey(modulePicture.getModuleId());
            if(ValidCheck.validPojo(module)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            //查询已添加图片数量，最多5张
            ModulePictureExample modulePictureExample = new ModulePictureExample();
            ModulePictureExample.Criteria criteria = modulePictureExample.or();
            criteria.andModuleIdEqualTo(modulePicture.getModuleId());
            List<ModulePicture> modulePictures = modulePictureMapper.selectByExample(modulePictureExample);

            if(modulePictures.size() >= 5){
                CommonResult.greatThan5(jsonResult);
                return jsonResult;
            }

            //上传图片

            String _key = storageService.uploadToPublicBucket("module/picture", file);
            if (StringUtils.isEmpty(_key)) {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }
            modulePicture.setPictureKey(_key);

            modulePictureMapper.insert(modulePicture);

            Map map = new HashMap();
            map.put("data", modulePicture);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    public JsonResult deletePictureByModuleIdAndPictureId(Integer moduleId, Integer pictureId, JsonResult jsonResult) {
        try {
            ModulePictureExample modulePictureExample = new ModulePictureExample();
            ModulePictureExample.Criteria criteria = modulePictureExample.or();
            criteria.andModuleIdEqualTo(moduleId);
            criteria.andIdEqualTo(pictureId);
            modulePictureMapper.deleteByExample(modulePictureExample);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public int insertModule(Module module) {
        return moduleMapper.insert(module);
    }

    @Override
    public int insertModulePicture(ModulePicture modulePicture) {
        return modulePictureMapper.insert(modulePicture);
    }

    @Override
    @Transactional
    public int insertModuleProductByModuleId(Integer moduleId, String productIds) {

        ModuleProductExample moduleProductExample = new ModuleProductExample();
        ModuleProductExample.Criteria criteria = moduleProductExample.or();
        criteria.andModuleIdEqualTo(moduleId);

        List<ModuleProduct> moduleProducts = moduleProductMapper.selectByExample(moduleProductExample);

        String[] ids = productIds.split(",");

        if(ValidCheck.validList(moduleProducts)){
            //为空插入
            for(String id : ids){
                Product product = productMapper.selectByPrimaryKey(Integer.parseInt(id));
                ModuleProduct moduleProduct = new ModuleProduct();
                moduleProduct.setCreateTime(new Timestamp(System.currentTimeMillis()));
                moduleProduct.setIntegral(product.getIntegral());
                moduleProduct.setModuleId(moduleId);
                moduleProduct.setName(product.getName());
                moduleProduct.setProductId(product.getId());
                moduleProduct.setSort(99);

                moduleProductMapper.insert(moduleProduct);
            }
        }else{
            //不为空更新
            if(moduleProducts.size() == ids.length){
                int index = 0;
                for(String id :ids){
                    Product product = productMapper.selectByPrimaryKey(Integer.parseInt(id));
                    ModuleProduct moduleProduct = moduleProducts.get(index);
                    moduleProduct.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    moduleProduct.setIntegral(product.getIntegral());
                    moduleProduct.setModuleId(moduleId);
                    moduleProduct.setName(product.getName());
                    moduleProduct.setProductId(product.getId());
                    moduleProduct.setSort(99);

                    moduleProductMapper.updateByPrimaryKey(moduleProduct);
                    index++;
                }
            }
        }


        return 0;
    }

    @Override
    public int deletePictureByModuleIdAndPictureId(Integer moduleId, Integer id) {
        ModulePictureExample modulePictureExample = new ModulePictureExample();
        ModulePictureExample.Criteria criteria = modulePictureExample.or();
        criteria.andModuleIdEqualTo(moduleId);
        criteria.andIdEqualTo(id);
        return modulePictureMapper.deleteByExample(modulePictureExample);
    }

    @Override
    public int deleteModuleProductByModuleIdAndId(Integer moduleId, Integer id) {
        ModuleProductExample moduleProductExample = new ModuleProductExample();
        ModuleProductExample.Criteria criteria = moduleProductExample.or();
        criteria.andIdEqualTo(id);
        criteria.andModuleIdEqualTo(moduleId);
        return moduleProductMapper.deleteByExample(moduleProductExample);
    }

    @Override
    public int deleteModuleByModule(Module module) {
        return moduleMapper.updateByPrimaryKey(module);
    }

    @Override
    @Transactional
    public int onlineModuleById(Module module, Plate plate) {
        moduleMapper.updateByPrimaryKey(module);
        plateMapper.insert(plate);
        plate.setSort(plate.getId());
        plateMapper.updateByPrimaryKey(plate);
        return 0;
    }

    @Override
    @Transactional
    public int offlineModuleById(Module module, Plate plate) {
        moduleMapper.updateByPrimaryKey(module);
        plateMapper.deleteByPrimaryKey(plate.getId());
        return 0;
    }


    @Override
    public List<ModulePicture> selectModulePictureByModuleId(Integer moduleId) {
        ModulePictureExample modulePictureExample = new ModulePictureExample();
        ModulePictureExample.Criteria criteria = modulePictureExample.or();
        criteria.andModuleIdEqualTo(moduleId);
        return modulePictureMapper.selectByExample(modulePictureExample);
    }

    @Override
    public List<ModuleProduct> selectModuleProductByModuleId(Integer moduleId) {
        ModuleProductExample moduleProductExample = new ModuleProductExample();
        ModuleProductExample.Criteria criteria = moduleProductExample.or();
        criteria.andModuleIdEqualTo(moduleId);

        List<ModuleProduct> moduleProducts = moduleProductMapper.selectByExample(moduleProductExample);
        if(!ValidCheck.validList(moduleProducts)){
            for (ModuleProduct moduleProduct : moduleProducts){
                Product product = productMapper.selectByPrimaryKey(moduleProduct.getProductId());
                moduleProduct.setLogoKey(product.getLogoKey());
            }

        }

        return moduleProducts;
    }

    @Override
    public Module selectModuleById(Integer moduleId) {
        return moduleMapper.selectByPrimaryKey(moduleId);
    }

    @Override
    public List<Module> selectModules() {
        ModuleExample moduleExample = new ModuleExample();
        ModuleExample.Criteria criteria = moduleExample.or();
        criteria.andStatusNotEqualTo(DBConstants.ModuleStatus.DELETED.getCode());
        moduleExample.setOrderByClause("id desc");
        return moduleMapper.selectByExample(moduleExample);
    }

    @Override
    public List<Module> selectModulesByEnabled() {
        ModuleExample moduleExample = new ModuleExample();
        ModuleExample.Criteria criteria = moduleExample.or();
        criteria.andStatusEqualTo(DBConstants.ModuleStatus.INACTIVE.getCode());
        moduleExample.setOrderByClause("id desc");
        return moduleMapper.selectByExample(moduleExample);
    }

    @Override
    public List<Plate> selectPlateByModuleId(Integer id) {
        PlateExample plateExample = new PlateExample();
        PlateExample.Criteria criteria = plateExample.or();
        criteria.andModuleIdEqualTo(id);

        return plateMapper.selectByExample(plateExample);
    }

}
