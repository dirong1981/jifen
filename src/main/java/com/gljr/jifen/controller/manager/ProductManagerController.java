package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.CommonResult;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductPhoto;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.service.ProductService;
import com.gljr.jifen.service.StorageService;
import com.gljr.jifen.service.StoreInfoService;
import com.qiniu.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/products")
public class ProductManagerController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StorageService storageService;


    @Autowired
    private StoreInfoService storeInfoService;


    /**
     * 查询所有商品包括未上架商品
     * @return
     */
    @GetMapping("/all")
    @ResponseBody
    public JsonResult selectProducts(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "per_page", required = false) Integer per_page){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(page)){
            page = 1;
        }

        if(StringUtils.isEmpty(per_page)){
            per_page = 10;
        }


        jsonResult = productService.selectAllProduct(page, per_page, jsonResult);

        return jsonResult;
    }



    /**
     * 下架商品
     * @param productId 商品id
     * @return 返回状态值
     */
    @GetMapping(value = "/{productId}/rejection")
    @ResponseBody
    public JsonResult stopProduct(@PathVariable("productId") Integer productId){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(productId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = productService.stopProductById(productId, jsonResult);


        return jsonResult;
    }


    /**
     * 商品上架
     * @param productId 商品id
     * @return 返回状态值
     */
    @GetMapping(value = "/{productId}/acceptance")
    @ResponseBody
    public JsonResult startProduct(@PathVariable("productId") Integer productId){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(productId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = productService.startProductById(productId, jsonResult);


        return jsonResult;
    }


    /**
     * 删除商品
     * @param productId 商品id
     * @return 返回状态值
     */
    @DeleteMapping(value = "/{productId}")
    @ResponseBody
    public JsonResult deleteProduct(@PathVariable("productId") Integer productId){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(productId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = productService.deleteProductById(productId, jsonResult);

        return jsonResult;
    }


    /**
     * 添加商品
     * @param product 商品模型
     * @param file 上传的商品缩略图
     * @return 返回状态码
     */
    @PostMapping
    @ResponseBody
    public JsonResult addProduct(Product product, @RequestParam(value="pic",required=false) MultipartFile file,
                                 @RequestParam(value = "random") Integer random){
        JsonResult jsonResult = new JsonResult();
        if(StringUtils.isEmpty(product.getSiId())){
            jsonResult.setMessage("请选择商户！");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }
        if(StringUtils.isEmpty(product.getCategoryCode()) || product.getCategoryCode() == -1){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage("请选择分类！");
            return jsonResult;
        }
//        String priceStr = httpServletRequest.getParameter("price");
//        int price = (int)(Double.parseDouble(priceStr) * 100);
//
//        product.setPrice(price);
        jsonResult = productService.insertProduct(product, file, random, jsonResult);

        return jsonResult;
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
    @PutMapping
    @ResponseBody
    public JsonResult updateProduct(@Valid Product product, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile,
                                    @RequestParam(value="pic",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();



        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jsonResult;
        }

        product.setCreateTime(new Timestamp(System.currentTimeMillis()));

        //上传图片

        if (file != null && !file.isEmpty()) {
            String _key = storageService.uploadToPublicBucket("product", file);
            if (StringUtils.isEmpty(_key)) {
                jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
                jsonResult.setMessage(GlobalConstants.UPLOAD_PICTURE_FAILED_MESSAGE);
                return jsonResult;
            }
            product.setLogoKey(_key);
        } else {
            product.setLogoKey(uploadfile);
        }

        try{
            //productService.updateProduct(product);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }


    /**
     * 用于多图上传
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadImages(@RequestParam(value="file",required=false) MultipartFile file, @RequestParam(value = "random") Integer random){
        JsonResult jsonResult = new JsonResult();
        //上传图片

        if(file == null || file.isEmpty() || StringUtils.isEmpty(random)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = productService.uploadFile(file, random, jsonResult);

        return jsonResult;
    }



}
