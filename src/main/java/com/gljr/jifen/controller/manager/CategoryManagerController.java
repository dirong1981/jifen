package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.service.CategoryService;
import com.gljr.jifen.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/v1/manager/categories")
public class CategoryManagerController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 添加分类
     *
     * @param category      分类模型
     * @param bindingResult 验证类
     * @param file          上传的图片
     * @return
     * @throws Exception
     */
    @PostMapping
    @ResponseBody
    public JsonResult addCategory(@Valid Category category, BindingResult bindingResult,
                                  @RequestParam(value = "pic", required = false) MultipartFile file){
        JsonResult jsonResult = new JsonResult();

        if (bindingResult.hasErrors()) {
            CommonResult.notNull(jsonResult);
            return jsonResult;
        }

        if (file != null && !file.isEmpty()) {
            String _key = storageService.uploadToPublicBucket("category", file);
            if (StringUtils.isEmpty(_key)) {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }
            category.setLogoKey(_key);
        } else {
            category.setLogoKey("category/default.png");
        }

        try {
            categoryService.insertClass(category);
            CommonResult.success(jsonResult);
        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    /**
     * 通过审核分类
     *
     * @param id 分类id
     * @return 返回状态吗
     */
    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    public JsonResult approveCategory(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            //通过id查询该分类
            Category category = categoryService.selectClass(id);

            if(ValidCheck.validPojo(category)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            category.setStatus(DBConstants.CategoryStatus.ACTIVED.getCode());
            categoryService.updateClass(category);

            CommonResult.success(jsonResult);
        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    /**
     * 取消审核分类
     *
     * @param id 分类id
     * @return 返回状态码
     */
    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    public JsonResult stopCategories(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            //通过id查询该分类
            Category category = categoryService.selectClass(id);
            if(ValidCheck.validPojo(category)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            category.setStatus(DBConstants.CategoryStatus.INACTIVE.getCode());

            categoryService.updateClass(category);
            CommonResult.success(jsonResult);
        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 删除一个分类
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult deleteCategories(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {

            //通过id查询该分类
            Category category = categoryService.selectClass(id);
            if(ValidCheck.validPojo(category)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            //判断该分类有没有子分类被使用
            List<Category> categories = categoryService.selectShowSonClass(category.getCode());
            if(!ValidCheck.validList(categories)){
                CommonResult.objIsUsed(jsonResult);
                return jsonResult;
            }

            //查询该分类有没有添加商品
            long count = categoryService.selectProductCountByCode(category.getCode());
            if(count > 0){
                CommonResult.objIsUsed(jsonResult);
                return jsonResult;
            }

            count = categoryService.selectStoreCountByCode(category.getCode());
            if(count > 0){
                CommonResult.objIsUsed(jsonResult);
                return jsonResult;
            }


            //通过code删除该分类和该分类下的子分类
            categoryService.deleteClass(category.getCode());

            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 给一个分类排序
     * @param id 分类id
     * @param sort 排序值
     * @return 返回状态码
     */
    @GetMapping(value = "/{id}/order/{sort}")
    @ResponseBody
    public JsonResult sortCategories(@PathVariable("id") Integer id, @PathVariable("sort") Integer sort){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id) || StringUtils.isEmpty(sort)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try{
            //通过id查询该分类
            Category category = categoryService.selectClass(id);

            if(ValidCheck.validPojo(category)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            category.setSort(sort);

            categoryService.updateClass(category);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return  jsonResult;
    }




    /**
     * 修改分类信息
     * @param category 分类模型
     * @param bindingResult 验证类
     * @param uploadfile 原分类图片名称
     * @param file 新上传的图片
     * @return 返回状态值
     */
    @PutMapping
    @ResponseBody
    public JsonResult updateClass(@Valid Category category, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile,
                                  @RequestParam(value="pic",required=false) MultipartFile file){
        JsonResult jsonResult = new JsonResult();


        category.setCreateTime(new Timestamp(System.currentTimeMillis()));

//        if(bindingResult.hasErrors()){
//            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
//            return jsonResult;
//        }

        //上传图片

        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path="";

        try {
            if (file != null && !file.isEmpty()) {
                //System.out.println("aaa");
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType=file.getContentType();
                //获得文件后缀名称
                String imageName=contentType.substring(contentType.indexOf("/")+1);
                String pic = UUID.randomUUID().toString().replaceAll("-","");
                String fileName = pic + "." + imageName;
                path="/WEB-INF/static/image/class-images/"+fileName;
                file.transferTo(new File(pathRoot+path));

                category.setLogoKey(fileName);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

            } else {
                category.setLogoKey(uploadfile);
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
        }

        try{
            categoryService.updateClass(category);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            Map map = new HashMap();
            map.put("cate", category);
            jsonResult.setItem(map);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }



    /**
     * 查询所有分类，包括未审核通过分类
     * @return
     */
    @GetMapping(value = "/all")
    @ResponseBody
    public JsonResult allCategories(){
        JsonResult jsonResult = new JsonResult();
        Map map = new HashMap();

        try{
            List<Category> list = null;

            list = categoryService.selectParentClass();
            map.put("parents", list);

            list = categoryService.selectSonClass();
            map.put("sons", list);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return  jsonResult;
    }


    /**
     * 查询所有商户分类，包括未审核分类
     * @return
     */
    @GetMapping(value = "/all/store")
    @ResponseBody
    public JsonResult allStoreCategories(){
        JsonResult jsonResult = new JsonResult();
        Map map = new HashMap();

        try{
            List<Category> list = null;

            list = categoryService.selectStoreParentClass();
            map.put("parents", list);

            list = categoryService.selectStoreSonClass();
            map.put("sons", list);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return  jsonResult;
    }


    /**
     * 查询所有通过审核商户和商品分类
     * @return
     */
    @GetMapping(value = "/sort")
    @ResponseBody
    public JsonResult allCategoriesIncludeProductStore(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = categoryService.allCategoriesIncludeProductStore(jsonResult);

        return  jsonResult;
    }

    @PutMapping(value = "/order")
    @ResponseBody
    public JsonResult changeCategoryOrder(@RequestParam(value = "cur") Integer cur, @RequestParam(value = "prev") Integer prev){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(cur) || StringUtils.isEmpty(prev)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = categoryService.changeCategoryOrder(cur, prev, jsonResult);

        return  jsonResult;
    }
}
