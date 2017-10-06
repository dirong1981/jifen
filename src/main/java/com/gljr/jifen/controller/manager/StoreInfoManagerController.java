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
     * 获取所有线下支付商户信息，包括未审核通过商户
     * @return 商户列表
     */
    @GetMapping("/all")
    @ResponseBody
    public JsonResult getAllStores() {
        JsonResult jsonResult = new JsonResult();

        jsonResult = storeInfoService.selectAllStoreInfo(jsonResult);

        return jsonResult;
    }


    /**
     * 获取所有线上商户信息，包括未审核通过商户
     * @return 商户列表
     */
    @GetMapping("/online/all")
    @ResponseBody
    public JsonResult getAllOnlineStores() {
        JsonResult jsonResult = new JsonResult();

        jsonResult = storeInfoService.selectAllOnlineStoreInfo(jsonResult);

        return jsonResult;
    }


    /**
     * 审核通过商户信息
     * @param storeId 商户id
     * @return 状态码
     */
    @GetMapping(value = "/{storeId}/acceptance")
    @ResponseBody
    public JsonResult startStore(@PathVariable("storeId") Integer storeId) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(storeId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = storeInfoService.startStoreInfo(storeId, jsonResult);


        return jsonResult;
    }


    /**
     * 下线商户
     * @param storeId 商户id
     * @return 状态码
     */
    @GetMapping(value = "/{storeId}/rejection")
    @ResponseBody
    public JsonResult stopStore(@PathVariable("storeId") Integer storeId) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(storeId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = storeInfoService.stopStoreInfo(storeId, jsonResult);

        return jsonResult;
    }


    /**
     * 删除一个商户,禁用管理员账号
     * @param storeId 商户id
     * @return 状态码
     */
    @DeleteMapping(value = "/{storeId}")
    @ResponseBody
    public JsonResult deleteStore(@PathVariable("storeId") Integer storeId) {
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(storeId)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = storeInfoService.deleteStoreInfo(storeId, jsonResult);


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

        if(bindingResult.hasErrors()){
            CommonResult.notNull(jsonResult);
            return jsonResult;
        }

        //线下扫码商户要进分类，线上商户不进分类，分类-1
        if(storeInfo.getStoreType() == DBConstants.MerchantType.OFFLINE.getCode() && storeInfo.getCategoryCode() == -1){
            jsonResult.setMessage("请选择商户分类！");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        if(storeInfo.getLocationCode() == -1 || StringUtils.isEmpty(storeInfo.getLocationCode())){
            jsonResult.setMessage("请选择地区！");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        //获取一个随机数，更新上传的图片id
        if(StringUtils.isEmpty(random)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        if(StringUtils.isEmpty(username)){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage("管理员账号不能为空！");
            return jsonResult;
        }

        jsonResult = storeInfoService.insertStoreInfo(storeInfo, file, username, random, jsonResult);


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

            //storeInfoService.updataStoreInfo(storeInfo);
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

        jsonResult = storeInfoService.uploadFile(random, file, jsonResult);

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

        jsonResult = storeInfoService.selectStoreInfoByKeyword(keyword, 1, 100, 0, jsonResult);


        return jsonResult;
    }


}
