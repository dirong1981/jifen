package com.gljr.jifen.controller;


import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.service.CategoryService;
import com.gljr.jifen.service.ProductService;
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
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping(value = "/jifen/class")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 视图
     * @param httpServletRequest
     * @return
     */

    //显示商品列表页面
    @RequestMapping(value = "/item2/page1", method = RequestMethod.GET)
    public ModelAndView item2Page(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item2/page1");
        return mv;
    }


    //显示添加商品页面
    @RequestMapping(value = "/item2/page1-add", method = RequestMethod.GET)
    public ModelAndView item2AddPage(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item2/page1-add");
        return mv;
    }




    /**
     * 获取启用父类别列表的ajax请求
     */
    @RequestMapping(value = "/item2/parentClassAjax", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult parentClassAjax(HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();

        try {
            //设置操作标记
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            //设置数据
            Map<Object, Object> map = new HashMap<>();
            List<Category> list = categoryService.selectShowParentClass();//获取大分类
            map.put("parentClass", list);
            jr.setItem(map);
        }catch (Exception e){
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }
        return jr;

    }



    /**
     * 获取启用子类别列表的ajax请求
     */
    @RequestMapping(value = "/item2/sonClassAjax", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult sonClassAjax(HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();

        try {
            //设置操作标记
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            //设置数据
            Map<Object, Object> map = new HashMap<>();
            List<Category> list = categoryService.selectShowSonClass();//获取大分类
            map.put("sonClass", list);
            jr.setItem(map);
        }catch (Exception e){
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }
        return jr;

    }


    @RequestMapping(value = "/item2/addProductAjax", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addClassAjax(@Valid Product product, BindingResult bindingResult, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();
        //System.out.printf(product.getBcId());
//        System.out.printf(product.getBcId());

        if(bindingResult.hasErrors()){
            jr.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jr;
        }

        String p_id = UUID.randomUUID().toString().replaceAll("-","");
        product.setpId(p_id);


        product.setpTime(new Timestamp(System.currentTimeMillis()));

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
                String fileName = p_id + "." + imageName;
                path = "/WEB-INF/static/image/product-images/" + fileName;
                file.transferTo(new File(pathRoot + path));

                product.setpLogo(fileName);

                //jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            }
        }catch (Exception e){
            jr.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
            return  jr;
        }


        try {
            productService.addProduct(product);

            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            //product.setpId(UUID.randomUUID().toString().replaceAll("-",""));
            //productService.insert(product);
        }catch (Exception e){
            System.out.println(e);
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jr;
    }



    //查询所有商品ajax
    @RequestMapping(value = "/item2/allProductAjax", method = RequestMethod.GET)
    @ResponseBody
    public Map allProductAjax(HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();


        try{
            //PageHelper.startPage(1,2);
            List<Product> list = productService.selectAllProduct();
            Map  map = new HashMap();
            map.put("data", list);


            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

            return map;
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return null;
    }


    @RequestMapping(value = "/item2/stopProductAjax/{id}")
    @ResponseBody
    public JsonResult stopProductAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();
        HttpSession httpSession = httpServletRequest.getSession();
        String p_creator = (String)httpSession.getAttribute(GlobalConstants.SESSION_ADMIN_ID);

        try {
            Product product = productService.selectProduct(id);
            product.setpState(0);
            product.setpCreator(p_creator);

            productService.updateProduct(product);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    @RequestMapping(value = "/item2/startProductAjax/{id}")
    @ResponseBody
    public JsonResult startProductAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();
        HttpSession httpSession = httpServletRequest.getSession();
        String p_creator = (String)httpSession.getAttribute(GlobalConstants.SESSION_ADMIN_ID);

        try {
            Product product = productService.selectProduct(id);
            product.setpState(1);
            product.setpCreator(p_creator);

            productService.updateProduct(product);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    /**
     * 删除一个商品
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/item2/deleteProductAjax/{id}")
    @ResponseBody
    public JsonResult deleteProductAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            productService.deleteProduct(id);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    /**
     * 打开商品编辑页面,获取一个商品
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/item2/updateProduct/{id}", method = RequestMethod.GET)
    public ModelAndView updateProductAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        ModelAndView modelAndView = new ModelAndView("/admin/item2/page1-update");

        Product product = productService.selectProduct(id);
        modelAndView.addObject("product",product);

        return modelAndView;
    }

    /**
     * 修改商品内容
     * @param product
     * @param bindingResult
     * @param uploadfile
     * @param file
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/item2/updateProduct", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateProduct(@Valid Product product, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jsonResult;
        }

        product.setpTime(new Timestamp(System.currentTimeMillis()));

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
                String fileName = product.getpId() + "." + imageName;
                path="/WEB-INF/static/image/product-images/"+fileName;
                file.transferTo(new File(pathRoot+path));

                product.setpLogo(fileName);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {
                //System.out.println("bbb");
                product.setpLogo(uploadfile);
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            System.out.println(e);
        }

        try{
            productService.updateProduct(product);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }
}
