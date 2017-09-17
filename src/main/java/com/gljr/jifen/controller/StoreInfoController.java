package com.gljr.jifen.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.JedisUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.Md5Util;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.util.YukiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
public class StoreInfoController {


    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private StrUtil strUtil;

    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private Md5Util md5Util;


    /**
     * 获取所有审核通过商户
     * @param page 当前页
     * @param per_page 每页显示数量
     * @param sort 排序方式
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 返回商户列表
     */
    @RequestMapping(value = "/stores", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getStores(@RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "per_page", required = false) Integer per_page, @RequestParam(value = "sort", required = false) Integer sort,
                                HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {


        JsonResult jsonResult = new JsonResult();


        try {

            //设置各个参数的默认值
            if(page == null){
                page = 1;
            }
            if(per_page == null){
                per_page = 10;
            }
            if(sort == null || sort > 2 || sort < 0){
                sort = 0;
            }

            PageHelper.startPage(page,per_page);
            List<StoreInfo> storeInfos = storeInfoService.selectAllShowStoreInfo(sort);

            PageInfo pageInfo = new PageInfo(storeInfos);
            Map  map = new HashMap();
            map.put("data", storeInfos);
            //设置总页面
            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }


    /**
     * 获取所有商户信息，包括未审核通过商户
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 商户列表
     */
    @RequestMapping(value = "/all-stores", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getAllStores(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {


        JsonResult jsonResult = new JsonResult();


        try {
            List<StoreInfo> storeInfos = storeInfoService.selectAllStoreInfo();

            Map  map = new HashMap();
            map.put("data", storeInfos);

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }


    /**
     * 获取一个商户信息
     * @param id 商户id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 商户详细信息
     */
    @RequestMapping(value = "/stores/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getStore(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        JsonResult jsonResult = new JsonResult();

        try {

            StoreInfo storeInfo = storeInfoService.selectStoreInfoById(id);

            Map map = new HashMap();
            map.put("data", storeInfo);

            List<StorePhoto> storePhotos = storeInfoService.selectStorePhotoById(id);
            map.put("photos", storePhotos);

            jsonResult.setItem(map);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }


    /**
     * 审核通过商户信息
     * @param id 商户uid
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @RequestMapping(value = "/stores/{id}/start", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult startStore(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        JsonResult jsonResult = new JsonResult();


        try {
            StoreInfo storeInfo = storeInfoService.selectStoreInfoById(id);

            storeInfo.setStatus(new Byte("1"));

            storeInfoService.updataStoreInfo(storeInfo);

            //更新商户管理员的状态
            Admin admin = adminService.selectAdminById(storeInfo.getAid());

            admin.setStatus(new Byte("1"));

            adminService.updateAdminById(admin);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }


        return jsonResult;
    }


    /**
     * 下线商户
     * @param id 商户id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @RequestMapping(value = "/stores/{id}/stop", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult stopStore(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        JsonResult jsonResult = new JsonResult();


        try {
            StoreInfo storeInfo = storeInfoService.selectStoreInfoById(id);

            storeInfo.setStatus(new Byte("2"));

            storeInfoService.updataStoreInfo(storeInfo);

            //更新商户管理员的状态
            Admin admin = adminService.selectAdminById(storeInfo.getAid());

            admin.setStatus(new Byte("2"));

            adminService.updateAdminById(admin);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }


        return jsonResult;
    }


    /**
     * 删除一个商户
     * @param id 商户id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @RequestMapping(value = "/stores/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResult deleteStore(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        JsonResult jsonResult = new JsonResult();


        try {
            StoreInfo storeInfo = storeInfoService.selectStoreInfoById(id);

            //删除管理员记录
            adminService.deleteAdminById(storeInfo.getAid());

            storeInfoService.deleteStoreInfoById(id);


            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }


        return jsonResult;
    }


    /**
     * 添加商户
     * @param storeInfo 商户模型
     * @param bindingResult 验证类
     * @param file 图片
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @RequestMapping(value = "/stores", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addAdminAjax(@Valid StoreInfo storeInfo, BindingResult bindingResult, @RequestParam(value = "username") String username, @RequestParam(value = "pic", required = false) MultipartFile file,
                                   HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        JsonResult jsonResult = new JsonResult();


        if (bindingResult.hasErrors()) {
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            jsonResult.setMessage(GlobalConstants.NOTNULL);
            return jsonResult;
        }


        //添加商户管理员账号
        try {
            List<Admin> admins = adminService.selectAdminByUsername(username);
            if (admins.size() != 0){
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage(GlobalConstants.USER_EXIST_STR);
                return jsonResult;
            }else{
                Admin admin = new Admin();
                admin.setUsername(username);
                admin.setCreateTime(new Timestamp(System.currentTimeMillis()));
                admin.setAccountType(new Byte("2"));
                admin.setStatus(new Byte("0"));
                String salt = strUtil.randomKey(32);
                admin.setSalt(salt);
                admin.setPassword(md5Util.md5("admin"+salt));

                adminService.insertAdmin(admin);

                storeInfo.setAid(admin.getId());
            }
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            return jsonResult;
        }


        //补充
        storeInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        storeInfo.setLocationCode(450200);//默认来柳州市的....这个该不会是要写一个联动的地区选择吧?
        storeInfo.setSerialCode(strUtil.randomKey(16));



        //上传图片
        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path = "";

        try {

            if (file != null && !file.isEmpty()) {
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                String fileName = YukiUtil.getUUID() + "." + imageName;
                path = "/WEB-INF/static/image/store-images/" + fileName;
                file.transferTo(new File(pathRoot + path));

                storeInfo.setLogoKey(fileName);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {
                storeInfo.setLogoKey("default.png");
            }
        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
        }

        try {

            storeInfoService.addStoreInfo(storeInfo);



            //查询存入商品的id
            int sid = storeInfo.getId();

            Jedis jedis = jedisUtil.getJedis();
            String sId = jedis.get("sId");

            //判断是不是有已经上传的商品图片，如果有，获取到保存以后的商品id，同时更新上传的图片里面的商品id
            if(!sId.equals("null")) {
                List<StorePhoto> storePhotos = storeInfoService.selectStorePhotoById(Integer.parseInt(sId));
                for (StorePhoto storePhoto : storePhotos) {
                    storePhoto.setSiId(sid);
                    storeInfoService.updateStroePhoto(storePhoto);
                }
                jedis.del("sId");
            }


            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
//            jr.setMessage("添加失败!");
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }


    /**
     * 修改一个商户信息
     * @param storeInfo 商户模型
     * @param bindingResult 验证类
     * @param uploadfile 原图片
     * @param file 新上传的图片
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @RequestMapping(value = "/stores/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateStore(@Valid StoreInfo storeInfo, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile,
                                  @RequestParam(value = "pic", required = false) MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        JsonResult jsonResult = new JsonResult();

//        if (bindingResult.hasErrors()) {
//            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
//            jsonResult.setMessage(GlobalConstants.NOTNULL);
//            return jsonResult;
//        }


        //上传图片
        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path = "";

        try {

            if (file != null && !file.isEmpty()) {
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                String fileName = YukiUtil.getUUID() + "." + imageName;
                path = "/WEB-INF/static/image/store-images/" + fileName;
                file.transferTo(new File(pathRoot + path));

                storeInfo.setLogoKey(fileName);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {
                storeInfo.setLogoKey(uploadfile);
            }
        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
        }

        try {

            storeInfoService.updataStoreInfo(storeInfo);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
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
    @RequestMapping(value = "/stores/uploadFiles")
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
                path = "/WEB-INF/static/image/store-images/" + fileName;
                file.transferTo(new File(pathRoot + path));


                //上传多张图片的时候先用一个随机数作为临时的商品id，待添加商品的时候再更新相同临时id
                Jedis jedis = jedisUtil.getJedis();
                String sId = jedis.get("sId");
                int sid = (int)(Math.random()*10000000);
                if(sId == null || sId.equals("null")){
                    jedis.set("sId", sid+"");
                    //productId = "10000";
                }else{
                    sid = Integer.parseInt(jedis.get("sId"));
                }
                StorePhoto storePhoto = new StorePhoto();
                storePhoto.setImgKey(fileName);
                storePhoto.setCreateTime(new Timestamp(System.currentTimeMillis()));
                storePhoto.setSiId(sid);
                storePhoto.setSort(1);
                storePhoto.setImgTitle("pic");
                storePhoto.setType(new Byte("1"));
                storeInfoService.insertStorePhoto(storePhoto);

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













    /**
     * 添加商户额外信息
     *
     * @return
     */
    @RequestMapping(value = "/storeinfos/add/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addAdminAjaxs(@Valid StoreExtInfo storeExtInfo, @PathVariable("id") String id, @RequestParam(value = "file-2", required = false) MultipartFile file, BindingResult bindingResult, HttpServletRequest httpServletRequest) {

        JsonResult jr = new JsonResult();


        if (bindingResult.hasErrors()) {
            jr.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jr;
        }


        //补充
        storeExtInfo.setCreateTime(new Date());
        storeExtInfo.setSiId(Integer.valueOf(id));//商户的ID


        //上传图片
        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path = "";

        try {

            if (file != null && !file.isEmpty()) {
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                String fileName = YukiUtil.getUUID() + "." + imageName;
                path = "/WEB-INF/static/image/store-images/" + fileName;
                file.transferTo(new File(pathRoot + path));

                storeExtInfo.setLicenseFileKey(fileName);

                jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            } else {
                storeExtInfo.setLicenseFileKey("default.png");
            }
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
        }

        try {

            storeInfoService.addStoreExt(storeExtInfo);
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jr.setMessage("添加成功!");
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//            jr.setMessage("添加失败!");
            jr.setMessage(e.toString());
        }

        return jr;
    }






    /**
     * 获取对应的商户额外信息
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/storeinfos/getStoreE/{id}")
    @ResponseBody
    public JsonResult getStoreE(@PathVariable("id") String id, HttpServletRequest httpServletRequest) {
        JsonResult jsonResult = new JsonResult();

        try {

            StoreExtInfo storeExtInfo = storeInfoService.getStoreExt(Integer.valueOf(id));


            if (storeExtInfo != null) {
                Map map = new HashMap();
                map.put("storeExtInfo", storeExtInfo);
                jsonResult.setItem(map);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            }
            else
                throw new Exception();



        } catch (Exception e) {
//            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }

}
