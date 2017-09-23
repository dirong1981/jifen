package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.JedisUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductPhoto;
import com.gljr.jifen.service.ProductService;
import com.gljr.jifen.service.StorageService;
import io.swagger.annotations.Api;
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
import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/products")
public class ProductManagerController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StorageService storageService;



    /**
     * 查询所有商品包括未上架商品
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping("/all")
    @ResponseBody
    public JsonResult allProducts(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
        try{
            List<Product> list = productService.selectAllProduct();

            Map map = new HashMap();
            map.put("data", list);


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
     * 下架商品
     * @param id 商品id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态值
     */
    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    public JsonResult stopProduct(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
            Product product = productService.selectProductById(id);
            product.setStatus(new Byte("0"));

            productService.updateProduct(product);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }


        return jsonResult;
    }


    /**
     * 商品上架
     * @param id 商品id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态值
     */
    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    public JsonResult startProduct(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
            Product product = productService.selectProductById(id);
            product.setStatus(new Byte("1"));

            productService.updateProduct(product);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }


        return jsonResult;
    }


    /**
     * 删除商品
     * @param id 商品id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态值
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult deleteProduct(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
            productService.deleteProduct(id);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }


        return jsonResult;
    }


    /**
     * 添加商品
     * @param product 商品模型
     * @param bindingResult 验证类
     * @param file 上传的商品缩略图
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态码
     */
    @PostMapping
    @ResponseBody
    public JsonResult addProduct(@Valid Product product, BindingResult bindingResult, @RequestParam(value="pic",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();



        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jsonResult;
        }

        String logoKey = UUID.randomUUID().toString().replaceAll("-","");
        //product.setpId(p_id);


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
            product.setLogoKey("product/default.png");
        }


        try {
            productService.addProduct(product);

            //查询存入商品的id
            int pid = product.getId();

            Jedis jedis = JedisUtil.getJedis();
            String productId = jedis.get("productId");

            //判断是不是有已经上传的商品图片，如果有，获取到保存以后的商品id，同时更新上传的图片里面的商品id
            if(!productId.equals("null")) {
                List<ProductPhoto> list = productService.selectProductPhoto(Integer.parseInt(productId));
                for (ProductPhoto productPhoto : list) {
                    productPhoto.setPid(pid);
                    productService.updateProductPhoto(productPhoto);
                }
                jedis.del("productId");
            }

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            //product.setpId(UUID.randomUUID().toString().replaceAll("-",""));
            //productService.insert(product);
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


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
    @AuthPassport(permission_code = "2")
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
            productService.updateProduct(product);
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
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadImages(@RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();




        //上传图片


        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path="";

        try {

            if (file != null && !file.isEmpty()) {
                String _key = storageService.uploadToPublicBucket("product", file);
                if (StringUtils.isEmpty(_key)) {
                    jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
                    jsonResult.setMessage(GlobalConstants.UPLOAD_PICTURE_FAILED_MESSAGE);
                    return jsonResult;
                }

                //上传多张图片的时候先用一个随机数作为临时的商品id，待添加商品的时候再更新相同临时id
                Jedis jedis = JedisUtil.getJedis();
                String productId = jedis.get("productId");
                int pid = (int)(Math.random()*10000000);
                if(productId == null || productId.equals("null")){
                    jedis.set("productId", pid+"");
                    //productId = "10000";
                }else{
                    pid = Integer.parseInt(jedis.get("productId"));
                }
                ProductPhoto productPhoto = new ProductPhoto();
                productPhoto.setImgKey(_key);
                productPhoto.setCreateTime(new Timestamp(System.currentTimeMillis()));
                productPhoto.setPid(pid);
                productPhoto.setSort(1);
                productService.insertProductPhoto(productPhoto);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {

            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
            System.out.println(e);
            return  jsonResult;
        }
        return jsonResult;
    }

}
