package com.gljr.jifen.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.CategoryService;
import com.gljr.jifen.service.ProductService;
import com.gljr.jifen.service.StoreInfoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreInfoService storeInfoService;



    /**
     * 查询所有通过审核的分类
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult getcategories(){

        JsonResult jsonResult = categoryService.selectCategories();

        return  jsonResult;
    }

    /**
     * 查询通过审核的商品分类
     * @return
     */
    @GetMapping(value = "/products")
    @ResponseBody
    public JsonResult selectProductCategories(){

        JsonResult jsonResult = categoryService.selectProductCategories();

        return  jsonResult;
    }


    /**
     * 获取所有通过审核的商户分类
     * @return
     */
    @GetMapping("/store")
    @ResponseBody
    public JsonResult getStorecategories(){

        JsonResult jsonResult = categoryService.selectStorecategories();

        return  jsonResult;
    }


    /**
     * 获取一个分类信息
     * @param id 分类id
     * @return 返回该分类的信息
     */
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult getOneCategories(@PathVariable("id") Integer id){

        JsonResult jsonResult = categoryService.selectCategoryById(id);

        return  jsonResult;
    }


    /**
     * 查询某个分类下的商品
     * @param code 分类code
     * @param page 页数
     * @param per_page 每页显示数量
     * @param sort 排序 0时间排序，1销量高到低，2销量低到高，3积分高到低，4积分低到高
     * @return 返回商品列表，总页数，商品总数，当前第几页
     */
    @GetMapping(value = "/{code}/products")
    @ResponseBody
    public JsonResult categoryProduct(@PathVariable("code") Integer code, @RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort){
        JsonResult jsonResult = new JsonResult();

        //设置各个参数的默认值
        if(page == null){
            page = 1;
        }
        if(per_page == null){
            per_page = 10;
        }
        if(sort == null || sort > 4 || sort < 0){
            sort = 0;
        }

        jsonResult = productService.selectProductByCode(code, page, per_page, sort, jsonResult);

        return jsonResult;
    }


    /**
     * 按分类查询商户
     * @param code
     * @param page
     * @param per_page
     * @param sort
     * @return
     */
    @GetMapping(value = "/{code}/stores")
    @ResponseBody
    public JsonResult categoryStore(@PathVariable("code") Integer code, @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort){
        JsonResult jsonResult = new JsonResult();


        if(StringUtils.isEmpty(code)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        //设置各个参数的默认值
        if(page == null){
            page = 1;
        }
        if(per_page == null){
            per_page = 10;
        }
        if(sort == null || sort > 4 || sort < 0){
            sort = 0;
        }

        jsonResult = storeInfoService.selectStoreInfoByCode(code, page, per_page, sort, jsonResult);

        return jsonResult;
    }


}
