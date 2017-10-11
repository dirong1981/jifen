package com.gljr.jifen.controller.manager;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Module;
import com.gljr.jifen.pojo.ModulePicture;
import com.gljr.jifen.pojo.ModuleProduct;
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
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insertModule(Module module, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        String aid = httpServletRequest.getHeader("aid");
        if(StringUtils.isEmpty(aid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        module.setCreateTime(new Timestamp(System.currentTimeMillis()));
        module.setManagerId(Integer.parseInt(aid));
        module.setThumbKey("module/default.png");
        module.setStatus(DBConstants.ModuleStatus.INACTIVE.getCode());

        jsonResult = moduleService.insertModuel(module, jsonResult);

        return jsonResult;
    }


    /**
     * 上传图片
     * @param moduleId
     * @param file
     * @param title
     * @param linkUrl
     * @return
     */
    @PostMapping("/{moduleId}/upload")
    @ResponseBody
    public JsonResult uploadPic(@PathVariable(value = "moduleId") Integer moduleId, @RequestParam(value="pic") MultipartFile file,
                                @RequestParam(value = "title") String title, @RequestParam(value = "linkUrl", required = false) String linkUrl,
                                @RequestParam(value = "banner", required = false) String banner){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(moduleId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        if(StringUtils.isEmpty(title)){
            jsonResult.setMessage("请添加标题！");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        if(StringUtils.isEmpty(linkUrl)){
            linkUrl = "";
        }

        if (file == null && file.isEmpty()) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage("请添加图片！");
            return jsonResult;
        }

        ModulePicture modulePicture = new ModulePicture();
        modulePicture.setCreateTime(new Timestamp(System.currentTimeMillis()));
        modulePicture.setLinkUrl(linkUrl);
        modulePicture.setModuleId(moduleId);
        modulePicture.setSort(99);
        modulePicture.setTitle(title);
        if(!StringUtils.isEmpty(banner)) {
            modulePicture.setBanner(Integer.parseInt(banner));
        }

        jsonResult = moduleService.uploadFile(file, modulePicture, jsonResult);

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

        jsonResult = moduleService.deletePictureByModuleIdAndPictureId(moduleId, id, jsonResult);

        return jsonResult;
    }


    /**
     * 添加一个模块下的商品
     * @param moduleId 模块id
     * @param productIds 商品id字符串
     * @return
     */
    @PostMapping(value = "/{moduleId}/products")
    @ResponseBody
    public JsonResult insertModuleProductByModuleId(@PathVariable(value = "moduleId") Integer moduleId, @RequestParam(value = "productIds") String productIds){
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
            if(module.getType() != DBConstants.ModuleType.PICTUREANDPRODUCT.getCode() && module.getType() != DBConstants.ModuleType.PRODUCT.getCode()){
                CommonResult.notAllowedOperation(jsonResult);
                return jsonResult;
            }
            productIds = productIds.substring(1, productIds.length());
            String[] ids = productIds.split(",");

            int num = 0;

            if(module.getExtType() == DBConstants.ModuleSecondType.PRODUCT2.getCode()){
                num = 2;
            }else if (module.getExtType() == DBConstants.ModuleSecondType.PRODUCT4.getCode()){
                num = 4;
            }else if (module.getExtType() == DBConstants.ModuleSecondType.PRODUCT6.getCode()){
                num = 6;
            }else if (module.getExtType() == DBConstants.ModuleSecondType.PRODUCT8.getCode()){
                num = 8;
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
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectModuls(@RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "per_page", required = false) Integer per_page){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(page)){
            page = 1;
        }

        if(StringUtils.isEmpty(per_page)){
            per_page = 10;
        }

        try {
            PageHelper.startPage(page,per_page);
            List<Module> modules = moduleService.selectModules();
            PageInfo pageInfo = new PageInfo(modules);

            Map map = new HashMap();
            map.put("data", modules);

            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

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
     * 查询模块下有多少商品
     * @param moduleId
     * @return
     */
    @GetMapping(value = "/{moduleId}/products")
    @ResponseBody
    public JsonResult selectModuleProducts(@PathVariable(value = "moduleId") Integer moduleId){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(moduleId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            List<ModuleProduct> moduleProducts = moduleService.selectModuleProductByModuleId(moduleId);
            Map map = new HashMap();
            map.put("data", moduleProducts);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    /**
     * 查询可以用的模块
     * @return
     */
    @GetMapping(value = "/enabled")
    @ResponseBody
    public JsonResult selectModulesByStatus(){
        JsonResult jsonResult = new JsonResult();

        try {
            List<Module> modules = moduleService.selectModulesByEnabled();
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
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        Module module = moduleService.selectModuleById(moduleId);
        if(ValidCheck.validPojo(module)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            module.setStatus(DBConstants.ModuleStatus.ACTIVED.getCode());

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
     * @return
     */
    @GetMapping(value = "/offline/{moduleId}")
    @ResponseBody
    public JsonResult offlineModuleById(@PathVariable("moduleId") Integer moduleId){
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
            module.setStatus(DBConstants.ModuleStatus.INACTIVE.getCode());
            moduleService.offlineModuleById(module,plates.get(0));
            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 删除一个模块
     * @param moduleId
     * @return
     */
    @DeleteMapping(value = "/{moduleId}")
    @ResponseBody
    public JsonResult deleteModuleById(@PathVariable(value = "moduleId") Integer moduleId){
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

            module.setStatus(DBConstants.ModuleStatus.DELETED.getCode());
            moduleService.deleteModuleByModule(module);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @PostMapping(value = "/{moduleId}/virtualproduct")
    @ResponseBody
    public JsonResult insertVirtualProduct(@PathVariable(value = "moduleId") Integer moduleId, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        System.out.println(moduleId);

        String p1 = httpServletRequest.getParameter("p1");
        String p2 = httpServletRequest.getParameter("p2");
        String p3 = httpServletRequest.getParameter("p3");
        String p4 = httpServletRequest.getParameter("p4");



        moduleService.insertVirtualProduct(p1,p2,p3,p4,moduleId,jsonResult);

        return jsonResult;
    }

}
