package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.pojo.Module;
import com.gljr.jifen.pojo.ModulePicture;
import com.gljr.jifen.pojo.Plate;
import com.gljr.jifen.service.ModuleService;
import com.gljr.jifen.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/modules")
public class ModuleManagerController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private StorageService storageService;


    /**
     * 添加一个模块信息
     * @param module
     * @param file
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insertModule(Module module, @RequestParam(value="pic",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
            String aid = httpServletRequest.getHeader("aid");
            //上传图片

            if (file != null && !file.isEmpty()) {
                String _key = storageService.uploadToPublicBucket("module", file);
                if (StringUtils.isEmpty(_key)) {
                    CommonResult.uploadFailed(jsonResult);
                    return jsonResult;
                }
                module.setThumbKey(_key);
            } else {
                module.setThumbKey("module/default.png");
            }

            module.setCreateTime(new Timestamp(System.currentTimeMillis()));
            module.setManagerId(Integer.parseInt(aid));
            module.setStatus(1);
            moduleService.insertModule(module);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }


    /**
     * 上传图片
     * @param moduleId
     * @param file
     * @param title
     * @param linkUrl
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/{moduleId}/upload")
    @ResponseBody
    public JsonResult uploadPic(@PathVariable(value = "moduleId") Integer moduleId, @RequestParam(value="pic") MultipartFile file,
                                @RequestParam(value = "title") String title, @RequestParam(value = "linkUrl", required = false) String linkUrl,
                                HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try{
            Module module = moduleService.selectModuleById(moduleId);
            if(ValidCheck.validPojo(module)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
            return jsonResult;
        }

        ModulePicture modulePicture = new ModulePicture();
        modulePicture.setCreateTime(new Timestamp(System.currentTimeMillis()));
        modulePicture.setLinkUrl(linkUrl);
        modulePicture.setModuleId(moduleId);
        modulePicture.setSort(99);
        modulePicture.setTitle(title);

        //查询已添加图片数量，最多5张
        List<ModulePicture> modulePictures = moduleService.selectModulePictureByModuleId(moduleId);

        if(modulePictures.size() >= 5){
            CommonResult.greatThan5(jsonResult);
            return jsonResult;
        }

        //上传图片
        if (file != null && !file.isEmpty()) {
            String _key = storageService.uploadToPublicBucket("module/picture", file);
            if (StringUtils.isEmpty(_key)) {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }
            modulePicture.setPictureKey(_key);
        } else {
            modulePicture.setPictureKey("module/picture/default.png");
        }

        try {
            moduleService.insertModulePicture(modulePicture);
            Map map = new HashMap();
            map.put("data", modulePicture);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 根据模块id和图片id删除一张图片
     * @param moduleId 模块id
     * @param id 图片id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @DeleteMapping(value = "/{moduleId}/picture/{id}")
    @ResponseBody
    public JsonResult deletePictureByModuleIdAndPictureId(@PathVariable(value = "moduleId") Integer moduleId, @PathVariable(value = "id") Integer id,
                                                          HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(moduleId) || StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            moduleService.deletePictureByModuleIdAndPictureId(moduleId, id);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 添加一个模块下的商品
     * @param moduleId 模块id
     * @param productIds 商品id字符串
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/{moduleId}/products")
    @ResponseBody
    public JsonResult insertModuleProductByModuleId(@PathVariable(value = "moduleId") Integer moduleId, @RequestParam(value = "productIds") String productIds,
                                                    HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(productIds) || StringUtils.isEmpty(moduleId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }



        try {
            //查询该模块的信息
            Module module = moduleService.selectModuleById(moduleId);

            //如果不存在，提示错误
            if(ValidCheck.validPojo(module)) {

                CommonResult.noObject(jsonResult);
                return jsonResult;

            }

            //如果该模块不是商品类型，提示错误
            if(module.getType() != 2 && module.getType() != 3){
                CommonResult.notAllowedOperation(jsonResult);
                return jsonResult;
            }

            String[] ids = productIds.split(",");

            int num = 0;

            if(module.getExtType() == 3){
                num = 2;
            }else if (module.getExtType() == 4){
                num = 4;
            }else if (module.getExtType() == 5){
                num = 6;
            }else{
                CommonResult.greatThan5(jsonResult);
                return jsonResult;
            }

            //判断该模块能上传的商品数量
            if(ids.length == num) {

                moduleService.insertModuleProductByModuleId(moduleId, productIds);
                CommonResult.success(jsonResult);
            }else{
                CommonResult.greatThan5(jsonResult);
                return jsonResult;
            }
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }

    /**
     * 通过模块id和商品id删除该模块下的一个商品
     * @param moduleId 模块id
     * @param productId 商品id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @DeleteMapping(value = "/{moduleId}/products/{id}")
    @ResponseBody
    public JsonResult deleteModuleProductByModuleIdAndId(@PathVariable(value = "moduleId") Integer moduleId, @PathVariable(value = "id") Integer productId,
                                                         HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(moduleId) || StringUtils.isEmpty(productId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            moduleService.deleteModuleProductByModuleIdAndId(moduleId, productId);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }


        return  jsonResult;
    }


    /**
     * 查询所有可用模块
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectModuls(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            List<Module> modules = moduleService.selectModules();
            Map map = new HashMap();
            map.put("data", modules);
            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return  jsonResult;
    }

    /**
     * 根据id获取一个模型
     * @param id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult selectModuleById(@PathVariable(value = "id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            Module module = moduleService.selectModuleById(id);
            Map map = new HashMap();
            map.put("data", module);
            jsonResult.setItem(map);

            CommonResult.success(jsonResult);
        }catch (Exception e){

        }

        return  jsonResult;
    }


    /**
     * 获取模块下的图片
     * @param moduleId
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/{moduleId}/pictures")
    @ResponseBody
    public JsonResult selectModulePictures(@PathVariable(value = "moduleId") Integer moduleId, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(moduleId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            List<ModulePicture> modulePictures = moduleService.selectModulePictureByModuleId(moduleId);
            Map map = new HashMap();
            map.put("data", modulePictures);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    /**
     * 按照状态查询模块
     * @param status
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping(value = "/status/{status}")
    @ResponseBody
    public JsonResult selectModulesByStatus(@PathVariable("status") Integer status, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(status)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            List<Module> modules = moduleService.selectModulesByStatus(status);
            Map map = new HashMap();
            map.put("data", modules);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 上线一个模块
     * @param moduleId
     * @param sort
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/online/{moduleId}")
    @ResponseBody
    public JsonResult onlineModuleToPlate(@PathVariable(value = "moduleId") Integer moduleId, @RequestParam(value = "sort") Integer sort,
                                          HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(moduleId)){
            System.out.println("aaa");
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        Module module = moduleService.selectModuleById(moduleId);
        System.out.println(module.getTitle());
        if(ValidCheck.validPojo(module)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            module.setStatus(0);

            String aid = httpServletRequest.getHeader("aid");

            Plate plate = new Plate();
            plate.setCreateTime(new Timestamp(System.currentTimeMillis()));
            plate.setManagerId(Integer.parseInt(aid));
            plate.setModuleId(moduleId);
            if (StringUtils.isEmpty(sort)) {
                sort = 9999;
            }
            plate.setSort(sort);
            moduleService.onlineModuleById(module, plate);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return  jsonResult;
    }


    /**
     * 下线一个首页模块
     * @param moduleId
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping(value = "/offline/{moduleId}")
    @ResponseBody
    public JsonResult offlineModuleById(@PathVariable("moduleId") Integer moduleId,
                                        HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(moduleId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        Module module = moduleService.selectModuleById(moduleId);
        if(ValidCheck.validPojo(module)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        List<Plate> plates = moduleService.selectPlateByModuleId(moduleId);
        if(ValidCheck.validList(plates)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            module.setStatus(1);
            moduleService.offlineModuleById(module,plates.get(0));
            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 删除一个模块，把模块的状态设置成2
     * @param moduleId
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping(value = "/{moduleId}")
    @ResponseBody
    public JsonResult deleteModuleById(@PathVariable(value = "moduleId") Integer moduleId, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(moduleId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }



        try {
            //不存在
            Module module = moduleService.selectModuleById(moduleId);
            if(ValidCheck.validPojo(module)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            //被使用
            List<Plate> plates = moduleService.selectPlateByModuleId(moduleId);
            if(!ValidCheck.validList(plates)){
                CommonResult.objIsUsed(jsonResult);
                return jsonResult;
            }

            module.setStatus(2);
            moduleService.deleteModuleByModule(module);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

}
