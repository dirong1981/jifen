package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.Module;
import com.gljr.jifen.pojo.ModulePicture;
import com.gljr.jifen.pojo.ModuleProduct;
import com.gljr.jifen.pojo.Plate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ModuleService {

    JsonResult insertModuel(Module module, JsonResult jsonResult);

    JsonResult uploadFile(MultipartFile file, ModulePicture modulePicture, JsonResult jsonResult);

    JsonResult deletePictureByModuleIdAndPictureId(Integer moduleId, Integer pictureId, JsonResult jsonResult);


    JsonResult insertVirtualProduct(String p1, String p2, String p3, String p4, Integer moduleId, JsonResult jsonResult);



    //添加

    /**
     * 添加一个模块
     * @param module
     * @return
     */
    int insertModule(Module module);

    /**
     * 添加一个模块图片
     * @param modulePicture
     * @return
     */
    int insertModulePicture(ModulePicture modulePicture);

    /**
     * 插入一个模块的商品
     * @param moduleId 模块id
     * @param productIds 商品的id集合
     * @return
     */
    int insertModuleProductByModuleId(Integer moduleId, String productIds);


    //删除

    /**
     * 通过id和模块id删除一张图片
     * @param moduleId
     * @param id
     * @return
     */
    int deletePictureByModuleIdAndPictureId(Integer moduleId, Integer id);

    /**
     * 根据模块id和商品id删除一个商品
     * @param moduleId
     * @param id
     * @return
     */
    int deleteModuleProductByModuleIdAndId(Integer moduleId, Integer id);


    /**
     * 删除一个模块，把模块的状态设置成2
     * @param module 要删除的模块
     * @return
     */
    int deleteModuleByModule(Module module);



    //修改


    /**
     * 上线一个模块
     * @param module
     * @param plate
     * @return
     */
    int onlineModuleById(Module module, Plate plate);


    /**
     * 下线一个模块
     * @param module
     * @param plate
     * @return
     */
    int offlineModuleById(Module module, Plate plate);

    //查询

    /**
     * 查询该模块下有几张图片
     * @param moduleId
     * @return
     */
    List<ModulePicture> selectModulePictureByModuleId(Integer moduleId);

    List<ModuleProduct> selectModuleProductByModuleId(Integer moduleId);

    /**
     * 通过id查询一个模块
     * @param moduleId
     * @return
     */
    Module selectModuleById(Integer moduleId);

    /**
     * 查询所有模块
     * @return
     */
    List<Module> selectModules(Integer ext_type);

    /**
     * 按照状态查询
     * @param status
     * @return
     */
    List<Module> selectModulesByEnabled();

    /**
     * 通过id查找一个首页在线模块
     * @param id
     * @return
     */
    List<Plate> selectPlateByModuleId(Integer id);



}
