package com.gljr.jifen.controller;


import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@Controller
@RequestMapping(value = "/v1")
public class ClassController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 查询所有通过审核的商品分类
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getcategories(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
        Map map = new HashMap();

        try{
            List<Category> list = null;

            list = categoryService.selectShowParentClass();
            map.put("parents", list);

            list = categoryService.selectShowSonClass();
            map.put("sons", list);

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }


    /**
     * 获取所有通过审核的商户分类
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/store-categories", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getStorecategories(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
        Map map = new HashMap();

        try{
            List<Category> list = null;

            list = categoryService.selectShowStoreParentClass();
            map.put("parents", list);

            list = categoryService.selectShowStoreSonClass();
            map.put("sons", list);

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }


    /**
     * 获取一个分类信息
     * @param id 分类id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回该分类的信息
     */
    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getOneCategories(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try{
            //通过id查询该分类
            Category category = categoryService.selectClass(id);
            Map map = new HashMap();
            map.put("category", category);

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }




    /**
     * 删除一个分类
     * @param id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult deleteCategories(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try{
            //通过id查询该分类
            Category category = categoryService.selectClass(id);
            //通过id删除该分类
            categoryService.deleteClass(id);

            //删除该分类下的子分类
            categoryService.deleteSonClass(category.getCode());
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }


    /**
     * 取消审核分类
     * @param id 分类id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态码
     */
    @RequestMapping(value = "/categories/{id}/stop", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult stopCategories(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try{
            //通过id查询该分类
            Category category = categoryService.selectClass(id);

            category.setStatus(new Byte("0"));

            //删除该分类下的子分类
            categoryService.updateClass(category);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }


    /**
     * 通过审核分类
     * @param id 分类id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态吗
     */
    @RequestMapping(value = "/categories/{id}/start", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult startCategories(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try{
            //通过id查询该分类
            Category category = categoryService.selectClass(id);

            category.setStatus(new Byte("1"));

            //删除该分类下的子分类
            categoryService.updateClass(category);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }


    /**
     * 给一个分类排序
     * @param id 分类id
     * @param sort 排序值
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态码
     */
    @RequestMapping(value = "/categories/{id}/sort/{sort}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult sortCategories(@PathVariable("id") Integer id, @PathVariable("sort") Integer sort, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try{
            //通过id查询该分类
            Category category = categoryService.selectClass(id);

            category.setSort(sort);

            //删除该分类下的子分类
            categoryService.updateClass(category);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return  jsonResult;
    }


    /**
     * 添加分类
     * @param category 分类模型
     * @param bindingResult 验证类
     * @param file 上传的图片
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addClassAjax(@Valid Category category, BindingResult bindingResult, @RequestParam(value="pic",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        JsonResult jsonResult = new JsonResult();



        //System.out.println(category.toString());
        int code = (int)(Math.random()*100000000);

        category.setCode(code);
        category.setCreateTime(new Timestamp(System.currentTimeMillis()));

        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jsonResult;
        }

        //生成分类id
        String pic = UUID.randomUUID().toString().replaceAll("-","");
        //category.setcId(c_id);


        //上传图片

        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path="";

        try {

            if (file != null && !file.isEmpty()) {
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                String fileName = pic + "." + imageName;
                path = "/WEB-INF/static/image/class-images/" + fileName;
                file.transferTo(new File(pathRoot + path));

                category.setLogoKey(fileName);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            }else{
                category.setLogoKey("default.png");
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
            jsonResult.setMessage(GlobalConstants.UPLOAD_PICTURE_FAILED_MESSAGE);
        }



        try {
            categoryService.insertClass(category);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }


        return jsonResult;
    }


    /**
     * 修改分类信息
     * @param category 分类模型
     * @param bindingResult 验证类
     * @param uploadfile 原分类图片名称
     * @param file 新上传的图片
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态值
     */
    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateClass(@Valid Category category, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile, @RequestParam(value="pic",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
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
     * 获取所有分类，包括为通过的分类
     * @return
     */
    @RequestMapping(value = "/allCategories", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult allCategories(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
        Map map = new HashMap();

        try{
            List<Category> list = null;

            list = categoryService.selectParentClass();
            map.put("parents", list);

            list = categoryService.selectSonClass();
            map.put("sons", list);

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jsonResult;
    }


    /**
     * 获取所有分类，包括为通过的分类
     * @return
     */
    @RequestMapping(value = "/allStoreCategories", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult allStoreCategories(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
        Map map = new HashMap();

        try{
            List<Category> list = null;

            list = categoryService.selectStoreParentClass();
            map.put("parents", list);

            list = categoryService.selectStoreSonClass();
            map.put("sons", list);

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jsonResult;
    }








}
