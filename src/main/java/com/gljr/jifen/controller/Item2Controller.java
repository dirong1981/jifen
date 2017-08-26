package com.gljr.jifen.controller;


import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/admin")
public class Item2Controller {

    //@Autowired
    //private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/item2/page1", method = RequestMethod.GET)
    public ModelAndView item2Page(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item2/page1");
        return mv;
    }

    @RequestMapping(value = "/item2/page1-add", method = RequestMethod.GET)
    public ModelAndView item2AddPage(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item2/page1-add");
        return mv;
    }
    /**
     * 获取商品类别列表的ajax请求
     */
    @RequestMapping(value = "/item2/parentClassAjax", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult classAjax(HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();
        //设置操作标记
        jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        //设置数据
        Map<Object,Object> map = new HashMap<>();
        List bigCategorys = categoryService.selectAllClass();//获取大分类
        map.put("bc",bigCategorys);
        jr.setItem(map);
        return jr;

    }


    @RequestMapping(value = "/item2/page1-add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addClassAjax(Product product, HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();
        //System.out.printf(product.getBcId());
//        System.out.printf(product.getBcId());
        Map map = new HashMap();
        map.put("product",product);
        jr.setItem(map);
        jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        //product.setpId(UUID.randomUUID().toString().replaceAll("-",""));
        //productService.insert(product);


        return jr;
    }
}
