package com.gljr.jifen.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.service.CategoryService;
import com.gljr.jifen.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping(value = "/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;


    /**
     * 查询所有通过审核的商品分类
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping
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
    @GetMapping("/store")
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
    @GetMapping("/{id}")
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
     * 查询某个分类下的商品
     * @param code 分类code
     * @param page 页数
     * @param per_page 每页显示数量
     * @param sort 排序 0时间排序，1销量高到低，2销量低到高，3积分高到低，4积分低到高
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回商品列表，总页数，商品总数，当前第几页
     */
    @GetMapping(value = "/{code}/products")
    @ResponseBody
    @ApiOperation(value = "根据分类code获取商品", httpMethod = "GET", notes = "code,page,per_page,sort", response = JsonResult.class)
    public JsonResult categoryProduct(@ApiParam(required = true, name = "code", value = "分类代码") @PathVariable("code") Integer code,
                                      @ApiParam(required = false, name = "page", value = "分当前页") @RequestParam(value = "page", required = false) Integer page,
                                      @ApiParam(required = false, name = "per_page", value = "每页显示数量") @RequestParam(value = "per_page", required = false) Integer per_page,
                                      @ApiParam(required = false, name = "sort", value = "排序方式") @RequestParam(value = "sort", required = false) Integer sort,
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
            //查询分类下的商品，按照sort排序
            List<Product> list = productService.selectCategoryProduct(code, sort);

            PageInfo pageInfo = new PageInfo(list);
            Map  map = new HashMap();
            map.put("data", list);
            //设置总页面
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
