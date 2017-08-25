package com.gljr.jifen.controller;


import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin")
public class Item1Controller {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/item1", method = RequestMethod.GET)
    public ModelAndView item1Page(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item1/page1");

        return mv;
    }

    @RequestMapping(value = "/item1/add", method = RequestMethod.GET)
    public ModelAndView item1AddPage(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/item1/add");

        return mv;
    }


    /**
     * 获取商品类别列表的ajax请求
     */
    @RequestMapping(value = "/item1/parentClassAjax", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult parentClassAjax(HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();

        List<Category> list = categoryService.selectParentClass();

        Map map = new HashMap<>();
        map.put("Category", list);

        jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        jr.setItem(map);

        return jr;

    }

    @RequestMapping(value = "/item1/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addClassAjax(@Valid Category category, BindingResult bindingResult, HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();

        if(bindingResult.hasErrors()){
            jr.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jr;
        }





        int result = categoryService.insertClass(category);

        if(result == 1){
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }else{
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jr;
    }




    //查询所有子类型ajax
    @RequestMapping(value = "/item1/sonClassAjax", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult sonClassAjax(HttpServletRequest httpServletRequest){
        JsonResult jr = new JsonResult();

        List<Category> list = categoryService.selectSonClass();

        Map map = new HashMap();
        map.put("sonClass",list);

        jr.setErrorCode("200");
        jr.setItem(map);

        return  jr;
    }


    //删除分类ajax
    @RequestMapping(value = "/item1/deleteClassAjax/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult deleteClassAjax(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        int result = categoryService.deleteClass(id);

        if(result == 1){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }else{
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }
}
