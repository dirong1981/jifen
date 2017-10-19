package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.service.CategoryService;
import com.gljr.jifen.service.SerialNumberService;
import com.gljr.jifen.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
@RestController
@RequestMapping(value = "/v1/manager/categories")
public class CategoryManagerController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private SerialNumberService serialNumberService;

    /**
     * 添加分类
     *
     * @param category      分类模型
     * @param file          上传的图片
     * @return
     * @throws Exception
     */
    @PostMapping
    @ResponseBody
    @AuthPassport(permission_code = "#16100101#")
    public JsonResult insertCategory(Category category, @RequestParam(value = "pic", required = false) MultipartFile file){
        JsonResult jsonResult = new JsonResult();


        if (file != null && !file.isEmpty()) {
            String _key = storageService.uploadToPublicBucket("category", file);
            if (StringUtils.isEmpty(_key)) {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }
            category.setLogoKey(_key);
        } else {
            category.setLogoKey("category/default.png");
        }

        category.setCode(this.serialNumberService.getNextCategoryCode(category));
        category.setStatus(DBConstants.CategoryStatus.INACTIVE.getCode());
        category.setCreateTime(new Timestamp(System.currentTimeMillis()));

        jsonResult = categoryService.insertCategory(category);

        return jsonResult;
    }

    /**
     * 通过审核分类
     *
     * @param id 分类id
     * @return 返回状态吗
     */
    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    @AuthPassport(permission_code = "#16100102#")
    public JsonResult startCategory(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = categoryService.startCategoryById(id);

        return jsonResult;
    }

    /**
     * 取消审核分类
     *
     * @param id 分类id
     * @return 返回状态码
     */
    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    @AuthPassport(permission_code = "#16100102#")
    public JsonResult stopCategories(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = categoryService.stopCategoryById(id);

        return jsonResult;
    }


    /**
     * 删除一个分类
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    @AuthPassport(permission_code = "#16100104#")
    public JsonResult deleteCategories(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = categoryService.deleteCategoryById(id);
        return jsonResult;
    }


    /**
     * 查询所有商品分类，包括未审核通过分类
     * @return
     */
    @GetMapping(value = "/all")
    @ResponseBody
    @AuthPassport(permission_code = "#161001#")
    public JsonResult allProductCategories(){

        JsonResult jsonResult = categoryService.selectAllProductCategories();

        return  jsonResult;
    }


    /**
     * 查询所有商户分类，包括未审核分类
     * @return
     */
    @GetMapping(value = "/all/store")
    @ResponseBody
    @AuthPassport(permission_code = "#161002#")
    public JsonResult allStoreCategories(){

        JsonResult jsonResult = categoryService.selectAllStoreCategories();

        return  jsonResult;
    }


    /**
     * 查询所有通过审核商户和商品分类
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult allCategoriesIncludeProductStore(){

        JsonResult jsonResult = categoryService.selectCategories();

        return  jsonResult;
    }

    /**
     * 排序
     * @param cur
     * @param prev
     * @return
     */
    @PutMapping(value = "/order")
    @ResponseBody
    public JsonResult changeCategoryOrder(@RequestParam(value = "cur") Integer cur, @RequestParam(value = "prev") Integer prev){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(cur) || StringUtils.isEmpty(prev)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = categoryService.changeCategoryOrder(cur, prev, jsonResult);

        return  jsonResult;
    }


    /**
     * 查询通过审核的商品分类
     * @return
     */
    @GetMapping(value = "/products")
    @ResponseBody
    public JsonResult selectEnabelProductCategory(){

        JsonResult jsonResult = categoryService.selectProductCategories();
        return jsonResult;
    }


    /**
     * 查询通过审核的商户分类
     * @return
     */
    @GetMapping(value = "/stores")
    @ResponseBody
    public JsonResult selectEnabelStoresCategory(){
        JsonResult jsonResult = categoryService.selectStorecategories();
        return jsonResult;
    }
}
