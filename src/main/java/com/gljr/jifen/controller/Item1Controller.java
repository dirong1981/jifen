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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/admin")
public class Item1Controller {

    @Autowired
    private CategoryService categoryService;

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




    /**
     * 获取商品父类别列表的ajax请求
     */
    @RequestMapping(value = "/item1/parentClassAjax", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult parentClassAjax(HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();

        try {
            List<Category> list = categoryService.selectParentClass();

            Map map = new HashMap<>();
            map.put("Category", list);

            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jr.setItem(map);
        }catch (Exception e){
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jr;

    }


    /**
     * 添加类别
     * @param category
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/item1/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addClassAjax(@Valid Category category, BindingResult bindingResult, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest) throws Exception{
        JsonResult jr = new JsonResult();

        //System.out.println(category.toString());

        if(bindingResult.hasErrors()){
            jr.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jr;
        }

        //生成分类id
        String c_id = UUID.randomUUID().toString().replaceAll("-","");
        category.setcId(c_id);


        //上传图片

        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path="";

        if(!file.isEmpty()){
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType=file.getContentType();
            //获得文件后缀名称
            String imageName=contentType.substring(contentType.indexOf("/")+1);
            String fileName = c_id + "." + imageName;
            path="/WEB-INF/static/image/class-images/"+fileName;
            file.transferTo(new File(pathRoot+path));

            category.setcLogo(fileName);

            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }



        try {
            int result = categoryService.insertClass(category);

            if (result == 1) {
                jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {
                jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
            }
        }catch (Exception e){
            System.out.println(e);
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jr;
    }




    //查询所有子类型ajax
    @RequestMapping(value = "/item1/sonClassAjax", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult sonClassAjax(HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();

        try {
            List<Category> list = categoryService.selectSonClass();

            Map map = new HashMap();
            map.put("sonClass", list);

            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jr.setItem(map);
        }catch (Exception e){
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jr;
    }


    //删除分类ajax
    @RequestMapping(value = "/item1/deleteClassAjax/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult deleteClassAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            int result = categoryService.deleteClass(id);

            if (result == 1) {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }


    //查询所有类型ajax
    @RequestMapping(value = "/item1/allClassAjax", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult allClassAjax(HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();

        try {
            List<Category> list = categoryService.selectSonClass();
            List<Category> list2 = categoryService.selectParentClass();

            Map map = new HashMap();
            map.put("sonClass", list);
            map.put("parentClass", list2);

            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jr.setItem(map);
        }catch (Exception e){
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jr;
    }


    //停用分类
    @RequestMapping(value = "/item1/stopClassAjax/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult stopClassAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();




        try{
            Category category = categoryService.selectClass(id);
            category.setcState(0);

            categoryService.updateClass(category);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jsonResult;
    }


    //启用分类
    @RequestMapping(value = "/item1/startClassAjax/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult startClassAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();




        try{
            Category category = categoryService.selectClass(id);
            category.setcState(1);

            categoryService.updateClass(category);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jsonResult;
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


    //修改分类
    @RequestMapping(value = "/item1/updateClass", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateClass(@Valid Category category, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest){
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
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
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


    //排序ajax
    @RequestMapping(value = "/item1/updateSortAjax/{id}/{sort}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult updateSortAjax(@PathVariable("id") String id, @PathVariable("sort") String sort, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try{
            categoryService.updateClassSort(sort, id);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return  jsonResult;
    }
}
