package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.JedisUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.Md5Util;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.ProductPhoto;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.pojo.StorePhoto;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.StorageService;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.util.YukiUtil;
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
@RequestMapping(value = "/v1/manager/stores")
public class StoreInfoManagerController extends BaseController {

    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private StorageService storageService;



    /**
     * 获取所有商户信息，包括未审核通过商户
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 商户列表
     */
    @GetMapping("/all")
    @ResponseBody
    public JsonResult getAllStores(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {


        JsonResult jsonResult = new JsonResult();


        try {
            List<StoreInfo> storeInfos = storeInfoService.selectAllStoreInfo();

            Map map = new HashMap();
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
     * 审核通过商户信息
     * @param id 商户uid
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @GetMapping(value = "/{id}/acceptance")
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
    @GetMapping(value = "/{id}/rejection")
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
    @DeleteMapping(value = "/{id}")
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
    @PostMapping
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
                jsonResult.setMessage(GlobalConstants.STORE_ADMIN_EXIST);
                return jsonResult;
            }else{
                Admin admin = new Admin();
                admin.setUsername(username);
                admin.setCreateTime(new Timestamp(System.currentTimeMillis()));
                admin.setAccountType(new Byte("2"));
                admin.setStatus(new Byte("0"));
                String salt = StrUtil.randomKey(32);
                admin.setSalt(salt);
                admin.setPassword(Md5Util.md5("admin"+salt));

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
        storeInfo.setSerialCode(StrUtil.randomKey(16));



        //上传图片
        //获得物理路径webapp所在路径
        String pathRoot = httpServletRequest.getSession().getServletContext().getRealPath("");
        String path = "";

        try {

            if (file != null && !file.isEmpty()) {
                String _key = storageService.uploadToPublicBucket("store", file);
                if (StringUtils.isEmpty(_key)) {
                    jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
                    jsonResult.setMessage(GlobalConstants.UPLOAD_PICTURE_FAILED_MESSAGE);
                    return jsonResult;
                }
                storeInfo.setLogoKey(_key);
            } else {
                storeInfo.setLogoKey("store/default.png");
            }
        } catch (Exception e) {
            jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
        }

        try {

            storeInfoService.addStoreInfo(storeInfo);



            //查询存入商品的id
            int sid = storeInfo.getId();

            Jedis jedis = JedisUtil.getJedis();
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
    @PutMapping
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
                String _key = storageService.uploadToPublicBucket("store", file);
                if (StringUtils.isEmpty(_key)) {
                    jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
                    jsonResult.setMessage(GlobalConstants.UPLOAD_PICTURE_FAILED_MESSAGE);
                    return jsonResult;
                }
                storeInfo.setLogoKey(_key);
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

                String _key = storageService.uploadToPublicBucket("store", file);
                if (StringUtils.isEmpty(_key)) {
                    jsonResult.setErrorCode(GlobalConstants.UPLOAD_PICTURE_FAILED);
                    jsonResult.setMessage(GlobalConstants.UPLOAD_PICTURE_FAILED_MESSAGE);
                    return jsonResult;
                }

                //上传多张图片的时候先用一个随机数作为临时的商品id，待添加商品的时候再更新相同临时id
                Jedis jedis = JedisUtil.getJedis();
                String sId = jedis.get("sId");
                int sid = (int)(Math.random()*10000000);
                if(sId == null || sId.equals("null")){
                    jedis.set("sId", sid+"");
                    //productId = "10000";
                }else{
                    sid = Integer.parseInt(jedis.get("sId"));
                }
                StorePhoto storePhoto = new StorePhoto();
                storePhoto.setImgKey(_key);
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


}
