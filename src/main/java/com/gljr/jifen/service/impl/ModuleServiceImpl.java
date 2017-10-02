package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;


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


        String[] ids = productIds.split(",");
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
    public Module selectModuleById(Integer moduleId) {
        return moduleMapper.selectByPrimaryKey(moduleId);
    }

    @Override
    public List<Module> selectModules() {
        ModuleExample moduleExample = new ModuleExample();
        ModuleExample.Criteria criteria = moduleExample.or();
        criteria.andStatusLessThanOrEqualTo(1);
        moduleExample.setOrderByClause("id desc");
        return moduleMapper.selectByExample(moduleExample);
    }

    @Override
    public List<Module> selectModulesByStatus(Integer status) {
        ModuleExample moduleExample = new ModuleExample();
        ModuleExample.Criteria criteria = moduleExample.or();
        criteria.andStatusEqualTo(status);
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
