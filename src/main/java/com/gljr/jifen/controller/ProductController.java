package com.gljr.jifen.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductPhoto;
import com.gljr.jifen.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 查询一个具体的商品
     * @param productId 商品id
     * @return 商品详细信息和商品大图信息
     */
    @GetMapping(value = "/{productId}")
    @ResponseBody
    public JsonResult oneProduct(@PathVariable("productId") Integer productId){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(productId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = productService.selectProductById(productId, jsonResult);

        return jsonResult;
    }


    /**
     * 关键字搜索商品
     * @param keyword 关键字
     * @param page 当前页
     * @param per_page 每页显示数量
     * @param sort 排序规则 0时间排序，1销量高到低，2销量低到高，3积分高到低，4积分低到高
     * @return 返回商品列表，总页数，商品总数，当前第几页
     */
    @GetMapping("/keyword/{keyword}")
    @ResponseBody
    public JsonResult searchProduct(@PathVariable("keyword") String keyword, @RequestParam(value = "page", required = false) Integer page,
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

        jsonResult = productService.selectProductByKeyword(keyword, page,per_page, sort, jsonResult);

        return jsonResult;
    }


}
