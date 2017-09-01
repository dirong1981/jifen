package com.gljr.jifen.controller;


import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@Controller
@RequestMapping(value = "/jifen")
public class ClassController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 视图跳转
     */


    /**
     * 设置页面跳转
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/item1", method = RequestMethod.GET)
    public ModelAndView item1Page(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item1/page1");

        return mv;
    }


    /**
     * 打开添加类别页面
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/item1/add", method = RequestMethod.GET)
    public ModelAndView item1AddPage(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item1/add");

        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute("parent_id", "0");

        return mv;
    }

    @RequestMapping(value = "/item1/add/{id}", method = RequestMethod.GET)
    public ModelAndView item1AddSonPage(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item1/add");

        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute("parent_id", id);

        return mv;
    }


    //修改分类
    @RequestMapping(value = "/item1/updateClass/{id}", method = RequestMethod.GET)
    public ModelAndView updateClass(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item1/update");

        try{
            Category category = categoryService.selectClass(id);
            mv.addObject("category",category);
        }catch (Exception e){

        }

        return mv;
    }







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
                categoryService.deleteClass(id);
                categoryService.deleteSonClass(id);
            }else if(type.equals("sort")){
                Category category = categoryService.selectClass(id);
                category.setcSort(sort);
                categoryService.updateClass(category);
            }else if(type.equals("stop")){
                Category category = categoryService.selectClass(id);
                category.setcState(0);
                categoryService.updateClass(category);
            }else if(type.equals("start")){
                Category category = categoryService.selectClass(id);
                category.setcState(1);
                categoryService.updateClass(category);
            }else if(type.equals("update")){
                Category category = categoryService.selectClass(id);

                map.put("categories",category);
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
    public JsonResult addClassAjax(@Valid Category category, BindingResult bindingResult, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        JsonResult jsonResult = new JsonResult();

        //System.out.println(category.toString());

        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jsonResult;
        }

        //生成分类id
        String c_id = UUID.randomUUID().toString().replaceAll("-","");
        category.setcId(c_id);


        //上传图片

        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path="";

        try {

            if (!file.isEmpty()) {
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                String fileName = c_id + "." + imageName;
                path = "/WEB-INF/static/image/class-images/" + fileName;
                file.transferTo(new File(pathRoot + path));

                category.setcLogo(fileName);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
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
    public JsonResult updateClass(@Valid Category category, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();


        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jsonResult;
        }

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
                String fileName = category.getcId() + "." + imageName;
                path="/WEB-INF/static/image/class-images/"+fileName;
                file.transferTo(new File(pathRoot+path));

                category.setcLogo(fileName);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {
                //System.out.println("bbb");
                category.setcLogo(uploadfile);
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
            System.out.println(e);
        }

        try{
            categoryService.updateClass(category);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }

}
