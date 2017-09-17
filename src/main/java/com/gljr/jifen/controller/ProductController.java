package com.gljr.jifen.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.JedisUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.Category;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductPhoto;
import com.gljr.jifen.service.CategoryService;
import com.gljr.jifen.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1")
public class ProductController {

    @Autowired
    private ProductService productService;


    @Autowired
    private JedisUtil jedisUtil;


    /**
     * 查询分类商品
     * @param code 分类code
     * @param page 页数
     * @param per_page 每页显示数量
     * @param sort 排序 0时间排序，1销量高到低，2销量低到高，3积分高到低，4积分低到高
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回商品列表，总页数，商品总数，当前第几页
     */
    @RequestMapping(value = "/categories/{code}/products", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult categoryProduct(@PathVariable("code") Integer code, @RequestParam(value = "page", required = false) Integer page,
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


    /**
     * 查询一个具体的商品
     * @param id 商品id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 商品详细信息和商品大图信息
     */
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/products/keyword/{keyword}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/products", method = RequestMethod.GET)
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


    /**
     * 下架商品
     * @param id 商品id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回状态值
     */
    @RequestMapping(value = "/products/{id}/stop", method = RequestMethod.GET)
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
    @RequestMapping(value = "/products/{id}/start")
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
    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
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
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addProduct(@Valid Product product, BindingResult bindingResult, @RequestParam(value="pic",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jr = new JsonResult();



        if(bindingResult.hasErrors()){
            jr.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jr;
        }

        String logoKey = UUID.randomUUID().toString().replaceAll("-","");
        //product.setpId(p_id);


        product.setCreateTime(new Timestamp(System.currentTimeMillis()));

        //上传图片

        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path="";

        try {
            if (file != null && !file.isEmpty()) {
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                String fileName = logoKey + "." + imageName;
                path = "/WEB-INF/static/image/product-images/" + fileName;
                file.transferTo(new File(pathRoot + path));

                product.setLogoKey(fileName);

                //jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            }else{
                product.setLogoKey("default.png");
            }
        }catch (Exception e){
            jr.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
            return  jr;
        }


        try {
            productService.addProduct(product);

            //查询存入商品的id
            int pid = product.getId();

            Jedis jedis = jedisUtil.getJedis();
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

            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            //product.setpId(UUID.randomUUID().toString().replaceAll("-",""));
            //productService.insert(product);
        }catch (Exception e){
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jr;
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
    @RequestMapping(value = "/products/update", method = RequestMethod.POST)
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

        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path="";

        try {
            if (file != null && !file.isEmpty()) {
                //System.out.println("aaa");
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType=file.getContentType();
                //获得文件后缀名称
                String logoKey = UUID.randomUUID().toString().replaceAll("-","");
                String imageName = contentType.substring(contentType.indexOf("/")+1);
                String fileName = logoKey + "." + imageName;
                path="/WEB-INF/static/image/product-images/"+fileName;
                file.transferTo(new File(pathRoot+path));

                product.setLogoKey(fileName);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {
                //System.out.println("bbb");
                product.setLogoKey(uploadfile);
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
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
    @RequestMapping(value = "/uploadFiles")
    @ResponseBody
    public JsonResult uploadImages(@RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();




        //上传图片


        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path="";

        try {
            if (file != null && !file.isEmpty()) {
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                String photo = UUID.randomUUID().toString().replaceAll("-","");
                String fileName = photo + "." + imageName;
                path = "/WEB-INF/static/image/product-images/" + fileName;
                file.transferTo(new File(pathRoot + path));


                //上传多张图片的时候先用一个随机数作为临时的商品id，待添加商品的时候再更新相同临时id
                Jedis jedis = jedisUtil.getJedis();
                String productId = jedis.get("productId");
                int pid = (int)(Math.random()*10000000);
                if(productId == null || productId.equals("null")){
                    jedis.set("productId", pid+"");
                    //productId = "10000";
                }else{
                    pid = Integer.parseInt(jedis.get("productId"));
                }
                ProductPhoto productPhoto = new ProductPhoto();
                productPhoto.setImgKey(fileName);
                productPhoto.setCreateTime(new Timestamp(System.currentTimeMillis()));
                productPhoto.setPid(pid);
                productPhoto.setSort(1);
                productService.insertProductPhoto(productPhoto);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            }else{
            }
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
            System.out.println(e);
            return  jsonResult;
        }
        return jsonResult;
    }
}
