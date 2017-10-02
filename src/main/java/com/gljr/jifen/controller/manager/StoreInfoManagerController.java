package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.StoreInfo;
import com.gljr.jifen.pojo.StorePhoto;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.SerialNumberService;
import com.gljr.jifen.service.StorageService;
import com.gljr.jifen.service.StoreInfoService;
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
@RequestMapping(value = "/v1/manager/stores")
public class StoreInfoManagerController extends BaseController {

    @Autowired
    private StoreInfoService storeInfoService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private SerialNumberService serialNumberService;



    /**
     * 获取所有商户信息，包括未审核通过商户
     * @return 商户列表
     */
    @GetMapping("/all")
    @ResponseBody
    public JsonResult getAllStores() {


        JsonResult jsonResult = new JsonResult();


        try {
            List<StoreInfo> storeInfos = storeInfoService.selectAllStoreInfo();

            Map map = new HashMap();
            map.put("data", storeInfos);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }



    /**
     * 审核通过商户信息
     * @param id 商户id
     * @return 状态码
     */
    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    public JsonResult startStore(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            StoreInfo storeInfo = storeInfoService.selectStoreInfoById(id);

            if(ValidCheck.validPojo(storeInfo)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            storeInfo.setStatus(DBConstants.MerchantStatus.ACTIVED.getCode());

            storeInfoService.updataStoreInfo(storeInfo);

            //更新商户管理员的状态
//            Admin admin = adminService.selectAdminById(storeInfo.getAid());
//
//            admin.setStatus(DBConstants.AdminAccountStatus.ACTIVED.getCode());
//
//            adminService.updateAdminById(admin);

            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }


    /**
     * 下线商户
     * @param id 商户id
     * @return 状态码
     */
    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    public JsonResult stopStore(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            StoreInfo storeInfo = storeInfoService.selectStoreInfoById(id);

            if(ValidCheck.validPojo(storeInfo)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            storeInfo.setStatus(DBConstants.MerchantStatus.OFFLINE.getCode());

            storeInfoService.updataStoreInfo(storeInfo);

            //更新商户管理员的状态
//            Admin admin = adminService.selectAdminById(storeInfo.getAid());
//
//            admin.setStatus(DBConstants.AdminAccountStatus.DISABLED.getCode());
//
//            adminService.updateAdminById(admin);

            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }


    /**
     * 删除一个商户
     * @param id 商户id
     * @return 状态码
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult deleteStore(@PathVariable("id") Integer id) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            StoreInfo storeInfo = storeInfoService.selectStoreInfoById(id);

            if(ValidCheck.validPojo(storeInfo)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            Admin admin = adminService.selectAdminById(storeInfo.getAid());

            if(ValidCheck.validPojo(admin)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            storeInfo.setStatus(DBConstants.MerchantStatus.DELETED.getCode());
            admin.setStatus(DBConstants.AdminAccountStatus.DISABLED.getCode());

            storeInfoService.deleteStoreInfoById(storeInfo, admin);


            CommonResult.success(jsonResult);

        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }


    /**
     * 添加商户
     * @param storeInfo 商户模型
     * @param bindingResult 验证类
     * @param file 图片
     * @return 状态码
     */
    @PostMapping
    @ResponseBody
    public JsonResult addAdminAjax(@Valid StoreInfo storeInfo, BindingResult bindingResult, @RequestParam(value = "username") String username,
                                   @RequestParam(value = "pic", required = false) MultipartFile file, @RequestParam(value = "random") Integer random) {

        JsonResult jsonResult = new JsonResult();


        if (bindingResult.hasErrors()) {
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            jsonResult.setMessage(GlobalConstants.NOTNULL);
            return jsonResult;
        }

        //获取一个随机数，更新上传的图片id
        if(StringUtils.isEmpty(random)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }


        //添加商户管理员账号
        List<Admin> admins = adminService.selectAdminByUsername(username);
        if(!ValidCheck.validList(admins)){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage("管理员账号已存在，请更换！");
            return jsonResult;
        }

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setCreateTime(new Timestamp(System.currentTimeMillis()));
        admin.setAccountType(DBConstants.AdminAccountType.STORE_ADMIN.getCode());
        admin.setStatus(DBConstants.AdminAccountStatus.INACTIVE.getCode());
        String salt = StrUtil.randomKey(32);
        admin.setSalt(salt);
        admin.setPassword(Md5Util.md5("admin"+salt));



        //上传图片
        if (file != null && !file.isEmpty()) {
            String _key = storageService.uploadToPublicBucket("store", file);
            if (StringUtils.isEmpty(_key)) {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }
            storeInfo.setLogoKey(_key);
        } else {
            storeInfo.setLogoKey("store/default.png");
        }

        try {
            storeInfo.setStatus(DBConstants.MerchantStatus.INACTIVE.getCode());
            storeInfo.setSerialCode(serialNumberService.gextNextStoreSerialCode(450204));
            storeInfo.setLocationCode(450204);
            storeInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));

            storeInfoService.addStoreInfo(storeInfo, admin, random);

            CommonResult.success(jsonResult);
        } catch (Exception e) {
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 修改一个商户信息
     * @param storeInfo 商户模型
     * @param bindingResult 验证类
     * @param uploadfile 原图片
     * @param file 新上传的图片
     * @return 状态码
     */
    @PutMapping
    @ResponseBody
    public JsonResult updateStore(@Valid StoreInfo storeInfo, BindingResult bindingResult, @RequestParam("uploadfile") String uploadfile,
                                  @RequestParam(value = "pic", required = false) MultipartFile file) {

        JsonResult jsonResult = new JsonResult();

//        if (bindingResult.hasErrors()) {
//            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
//            jsonResult.setMessage(GlobalConstants.NOTNULL);
//            return jsonResult;
//        }


        //上传图片

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
     * @return
     */
    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadImages(@RequestParam(value="file",required=false) MultipartFile file, @RequestParam(value = "random") Integer random){
        JsonResult jsonResult = new JsonResult();

        //上传图片

        if(StringUtils.isEmpty(random)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }


        try {
            long num = storeInfoService.selectStorePhotoCountBySiId(random);

            if(num >= 5){
                jsonResult.setMessage("最多上传5张图片！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }


            if (file != null && !file.isEmpty()) {

                String _key = storageService.uploadToPublicBucket("store", file);
                if (StringUtils.isEmpty(_key)) {
                    CommonResult.uploadFailed(jsonResult);
                    return jsonResult;
                }

                StorePhoto storePhoto = new StorePhoto();
                storePhoto.setImgKey(_key);
                storePhoto.setCreateTime(new Timestamp(System.currentTimeMillis()));
                storePhoto.setSiId(random);
                storePhoto.setSort(99);
                storePhoto.setImgTitle("pic");
                storePhoto.setType(1);
                storeInfoService.insertStorePhoto(storePhoto);

                CommonResult.success(jsonResult);
            }else{
            }
        }catch (Exception e){
            CommonResult.uploadFailed(jsonResult);
        }
        return jsonResult;
    }


    /**
     * 根据关键字查询商户
     * @param keyword
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @PostMapping("/keywords")
    @ResponseBody
    public JsonResult selectStoreInfoByKeyword(@RequestParam(value = "keyword") String keyword, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();
        if(StringUtils.isEmpty(keyword)){
            CommonResult.notNull(jsonResult);
            return jsonResult;
        }

        try {
            List<StoreInfo> storeInfos = storeInfoService.selectStroreInfoByKeyword(keyword);
            if(ValidCheck.validList(storeInfos)){
                jsonResult.setErrorCode("500");
                jsonResult.setMessage("没有找到商铺信息，请更换关键字！");
                return jsonResult;
            }
            Map map = new HashMap();
            map.put("data", storeInfos);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }


        return jsonResult;
    }


}
