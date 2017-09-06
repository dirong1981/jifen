package com.gljr.jifen.controller;


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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/jifen")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;



    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    @AuthPassport(permission_code = "200")
    public JsonResult addProduct(@Valid Product product, BindingResult bindingResult, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
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
            System.out.println(product.getId());

//            HttpSession httpSession = httpServletRequest.getSession();
//            int productId = (int) httpSession.getAttribute("productId");
//
//            List<ProductPhoto> list = productService.selectProductPhoto(productId);
//            for(ProductPhoto productPhoto: list){
//                productPhoto.setPid(pid);
//                productService.updateProductPhoto(productPhoto);
//            }

            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            //product.setpId(UUID.randomUUID().toString().replaceAll("-",""));
            //productService.insert(product);
        }catch (Exception e){
            System.out.println(e);
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jr;
    }


    /**
     * 查询所有商品
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseBody
    @AuthPassport(permission_code = "200")
    public Map allProductAjax(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();



        try{
            //PageHelper.startPage(1,2);
            List<Product> list = productService.selectAllProduct();
            Map  map = new HashMap();
            map.put("data", list);


            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

            return map;
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return null;
    }


    @RequestMapping(value = "/products/stop/{id}")
    @ResponseBody
    @AuthPassport(permission_code = "200")
    public JsonResult stopProductAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();



        try {
            Product product = productService.selectProduct(Integer.parseInt(id));
            product.setStatus(new Byte("0"));

            productService.updateProduct(product);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    @RequestMapping(value = "/products/start/{id}")
    @ResponseBody
    @AuthPassport(permission_code = "200")
    public JsonResult startProductAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();



        try {
            Product product = productService.selectProduct(Integer.parseInt(id));
            product.setStatus(new Byte("1"));

            productService.updateProduct(product);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    /**
     * 删除一个商品
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/products/delete/{id}")
    @ResponseBody
    @AuthPassport(permission_code = "200")
    public JsonResult deleteProductAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();



        try {
            productService.deleteProduct(Integer.parseInt(id));

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    @RequestMapping(value = "/products/update/{id}")
    @ResponseBody
    @AuthPassport(permission_code = "200")
    public JsonResult updateProductAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            Product product = productService.selectProduct(Integer.parseInt(id));

            Map map = new HashMap();
            map.put("products",product);
            jsonResult.setItem(map);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

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
    @RequestMapping(value = "/products/update", method = RequestMethod.POST)
    @ResponseBody
    @AuthPassport(permission_code = "200")
    public JsonResult updateProduct(@Valid Product product, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile, @RequestParam(value="file-2",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
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
                String imageName=contentType.substring(contentType.indexOf("/")+1);
                String fileName = product.getLogoKey() + "." + imageName;
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
            System.out.println(e);
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




    @RequestMapping(value = "/uploadFiles")
    @ResponseBody
    public JsonResult uploadImages(@RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();



        String photo = UUID.randomUUID().toString().replaceAll("-","");
        //上传图片

        HttpSession httpSession = httpServletRequest.getSession();
        System.out.println(httpSession.getId());

        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path="";

        try {
            if (file != null && !file.isEmpty()) {
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                String fileName = photo + "." + imageName;
                path = "/WEB-INF/static/image/product-images/" + fileName;
                file.transferTo(new File(pathRoot + path));


                int productId;

               // System.out.println(httpSession.getId());
                if(httpSession.getAttribute("productId") == null){
                    productId = (int)(Math.random()*100000);
                    httpSession.setAttribute("productId", productId);

                }else {
                    productId = (int) httpSession.getAttribute("productId");
                    System.out.println("====="+productId);
                }


//                httpSession.setAttribute("productId", productId);
//
//                ProductPhoto productPhoto = new ProductPhoto();
//                productPhoto.setImgKey(fileName);
//                productPhoto.setCreateTime(new Timestamp(System.currentTimeMillis()));
//                productPhoto.setPid(productId);
//                productPhoto.setSort(1);
//                productService.insertProductPhoto(productPhoto);

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
