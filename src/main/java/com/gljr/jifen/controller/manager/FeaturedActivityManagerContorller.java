package com.gljr.jifen.controller.manager;


import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.FeaturedActivity;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.FeaturedActivityService;
import com.gljr.jifen.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/featuredactivitys")
public class FeaturedActivityManagerContorller {

    @Autowired
    private FeaturedActivityService featuredActivityService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private StorageService storageService;

    /**
     * 查询所有精选页,状态0,1,2
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectFeaturedActivitys(){
        JsonResult jsonResult = new JsonResult();

        try {
            List<FeaturedActivity> featuredActivities = featuredActivityService.selectFeaturedActivitys();

            //没有找到
            if(!ValidCheck.validList(featuredActivities)){

                for(FeaturedActivity featuredActivitie : featuredActivities){

                    Admin admin = adminService.selectAdminById(featuredActivitie.getManagerId());

                    //没有找到
                    if(ValidCheck.validPojo(admin)){
                        CommonResult.noObject(jsonResult);
                        return jsonResult;
                    }

                    featuredActivitie.setAdmin(admin.getUsername());
                }
            }

            Map map = new HashMap();
            map.put("data", featuredActivities);

            CommonResult.success(jsonResult);
            jsonResult.setItem(map);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 添加一个精选内容
     * @param featuredActivity
     * @param bindingResult
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insertFeaturedActivity(@Valid FeaturedActivity featuredActivity, BindingResult bindingResult, @RequestParam(value="pic",required=false) MultipartFile file,
                                             HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

//        if(bindingResult.hasErrors()){
//            jsonResult.setMessage(GlobalConstants.NOTNULL);
//            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
//            return jsonResult;
//        }

        try {
            //获取开始结束时间
            String start = httpServletRequest.getParameter("activityStart");
            String end = httpServletRequest.getParameter("activityEnd");

            String aid = httpServletRequest.getHeader("aid");



            //判断站内站外
            if (featuredActivity.getLinkType() == 1) {
                String localUrl = "/moduleaggregations/" + httpServletRequest.getParameter("localUrl");
                featuredActivity.setLinkUrl(localUrl);
            } else{
                String outUrl = httpServletRequest.getParameter("outUrl");
                featuredActivity.setLinkUrl(outUrl);
            }


            //上传图片

            if (file != null && !file.isEmpty()) {
                String _key = storageService.uploadToPublicBucket("featuredactivity", file);
                if (StringUtils.isEmpty(_key)) {
                    CommonResult.uploadFailed(jsonResult);
                    return jsonResult;
                }
                featuredActivity.setPictureKey(_key);
            } else {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }


            //时间转换
            if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)){
                String pattern="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
                featuredActivity.setActivityEnd(dateFormat.parse(end));
                featuredActivity.setActivityStart(dateFormat.parse(start));
            }

            featuredActivity.setStatus(0);
            featuredActivity.setManagerId(Integer.parseInt(aid));
            featuredActivity.setCreateTime(new Timestamp(System.currentTimeMillis()));

            featuredActivityService.insertFeaturedActivity(featuredActivity);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 下架精选内容
     * @param id
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    public JsonResult stopFeaturedActivity(@PathVariable(value = "id") Integer id, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try{
            FeaturedActivity featuredActivity = featuredActivityService.selectFeaturedActivityById(id);
            //没有找到
            if(ValidCheck.validPojo(featuredActivity)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            featuredActivity.setStatus(3);
            featuredActivityService.updateFeaturedActivityById(featuredActivity);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 上架精选内容
     * @param id
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    public JsonResult startFeaturedActivity(@PathVariable(value = "id") Integer id, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try{
            FeaturedActivity featuredActivity = featuredActivityService.selectFeaturedActivityById(id);
            if(ValidCheck.validPojo(featuredActivity)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }
            featuredActivity.setStatus(1);
            featuredActivityService.updateFeaturedActivityById(featuredActivity);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


    /**
     * 删除一个精选页，状态设置为3
     * @param id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult deleteFeaturedActivitysById(@PathVariable(value = "id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        try {
            FeaturedActivity featuredActivity = featuredActivityService.selectFeaturedActivityById(id);
            if(ValidCheck.validPojo(featuredActivity)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            if(featuredActivity.getStatus() == 1){
                CommonResult.objIsUsed(jsonResult);
                return jsonResult;
            }

            featuredActivity.setStatus(3);
            featuredActivityService.deleteFeaturedActivitysById(featuredActivity);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

}
