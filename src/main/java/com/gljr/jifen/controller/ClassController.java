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


@Controller
@RequestMapping(value = "/jifen")
public class ClassController {

    @Autowired
    private CategoryService categoryService;


    /**
     * Get请求控制
     */


    /**
     *
     * 获取分类：all所有，parent父分类，son子分类
     * @param type
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/categories/{type}", method = RequestMethod.GET)
    @ResponseBody
    @AuthPassport(permission_code = "100")

    public JsonResult parentClassAjax(@PathVariable("type") String type, @RequestParam(value = "id", required = false) String id, @RequestParam(value = "sort", required = false) String sort, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();


        Map map = new HashMap<>();

        try {
            List<Category> list = null;
            if(type.equals("all")) {
                list = categoryService.selectAllClass();

                map.put("categories", list);
            }else if(type.equals("parent")){
                list = categoryService.selectParentClass();

                map.put("categories", list);
            }else if(type.equals("son")){
                list = categoryService.selectSonClass();

                map.put("categories", list);
            }else if(type.equals("delete")){
                Category category = categoryService.selectClass(Integer.parseInt(id));
                categoryService.deleteClass(Integer.parseInt(id));

                categoryService.deleteSonClass(category.getCode());
            }else if(type.equals("sort")){
                Category category = categoryService.selectClass(Integer.parseInt(id));
                category.setSort(Integer.parseInt(sort));
                categoryService.updateClass(category);
            }else if(type.equals("stop")){
                Category category = categoryService.selectClass(Integer.parseInt(id));
                category.setStatus(new Byte("0"));
                categoryService.updateClass(category);
            }else if(type.equals("start")){
                Category category = categoryService.selectClass(Integer.parseInt(id));
                category.setStatus(new Byte("1"));
                categoryService.updateClass(category);
            }else if(type.equals("update")){
                Category category = categoryService.selectClass(Integer.parseInt(id));

                map.put("categories",category);
            }else if(type.equals("showParent")){
                list = categoryService.selectShowParentClass();
                map.put("categories", list);
            }else if(type.equals("showSon")){
                list = categoryService.selectShowSonClass();
                map.put("categories", list);
            }

            jsonResult.setItem(map);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;

    }




    /**
     * 添加类别
     * @param category
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    @ResponseBody
    @AuthPassport(permission_code = "100")
    public JsonResult addClassAjax(@Valid Category category, BindingResult bindingResult, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
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
            }else{
                category.setLogoKey("default.png");
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
        }



        try {
            int result = categoryService.insertClass(category);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }





    /**
     * 修改分类内容
     * @param category
     * @param bindingResult
     * @param uploadfile
     * @param file
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    @ResponseBody
    @AuthPassport(permission_code = "100")
    public JsonResult updateClass(@Valid Category category, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
//        System.out.println(category.getId());
//        System.out.println(category.getName());
//        System.out.println(category.getType());
//        System.out.println(category.getParentCode());
//        System.out.println(category.getCode());
//        System.out.println(category.getSort());
//        System.out.println(category.getStatus());
//        System.out.println(category.getCreateTime());
//        System.out.println(category.getLogoKey());


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
                String fileName = category.getLogoKey() + "." + imageName;
                path="/WEB-INF/static/image/class-images/"+fileName;
                file.transferTo(new File(pathRoot+path));

                category.setLogoKey(fileName);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {
                //System.out.println("bbb");
                category.setLogoKey(uploadfile);
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
            System.out.println(e);
        }

        try{
            categoryService.updateClass(category);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }

}
