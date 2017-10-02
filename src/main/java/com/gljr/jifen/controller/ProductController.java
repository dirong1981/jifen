package com.gljr.jifen.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductPhoto;
import com.gljr.jifen.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
     * @param id 商品id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 商品详细信息和商品大图信息
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult oneProduct(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
        try{
            Product product = productService.selectProductById(id);
            Map  map = new HashMap();
            map.put("data", product);

            List<ProductPhoto> list = productService.selectProductPhotoById(id);

            map.put("photos", list);

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

            return jsonResult;
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }


    /**
     * 关键字搜索商品
     * @param keyword 关键字
     * @param page 当前页
     * @param per_page 每页显示数量
     * @param sort 排序规则 0时间排序，1销量高到低，2销量低到高，3积分高到低，4积分低到高
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回商品列表，总页数，商品总数，当前第几页
     */
    @GetMapping("/keyword/{keyword}")
    @ResponseBody
    public JsonResult searchProduct(@PathVariable("keyword") String keyword, @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                    HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
        try{

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


            PageHelper.startPage(page,per_page);
            List<Product> list = productService.selectProductByKeyword(keyword, sort);
            PageInfo pageInfo = new PageInfo(list);
            Map  map = new HashMap();
            map.put("data", list);

            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

            return jsonResult;
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }


    /**
     * 查询所有商品
     * @param page 总页数
     * @param per_page 每页显示数量
     * @param sort 排序规则 0时间排序，1销量高到低，2销量低到高，3积分高到低，4积分低到高
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回商品列表，总页数，商品总数，当前第几页
     */
    @GetMapping
    @ResponseBody
    public JsonResult allProduct(@RequestParam(value = "page", required = false) Integer page,
                                 @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                 HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
        try{

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


            PageHelper.startPage(page,per_page);
            List<Product> list = productService.selectAllProduct(sort);

            PageInfo pageInfo = new PageInfo(list);
            Map  map = new HashMap();
            map.put("data", list);

            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());


            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

            return jsonResult;
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }






}
