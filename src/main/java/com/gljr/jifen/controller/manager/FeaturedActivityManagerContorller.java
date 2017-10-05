package com.gljr.jifen.controller.manager;


import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.FeaturedActivity;
import com.gljr.jifen.service.FeaturedActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/featuredactivitys")
public class FeaturedActivityManagerContorller {

    @Autowired
    private FeaturedActivityService featuredActivityService;


    /**
     * 查询所有精选页,状态0,1,2
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectFeaturedActivitys(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = featuredActivityService.selectFeaturedActivitys(jsonResult);

        return jsonResult;
    }


    /**
     * 添加一个精选内容
     * @param featuredActivity
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult insertFeaturedActivity(FeaturedActivity featuredActivity, @RequestParam(value="pic", required = false) MultipartFile file,
                                             HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        //获取开始结束时间
        String start = httpServletRequest.getParameter("start");
        String end = httpServletRequest.getParameter("end");

        String aid = httpServletRequest.getHeader("aid");
        if(StringUtils.isEmpty(aid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }
        //判断站内站外
        if (featuredActivity.getLinkType() == 1) {
            String localUrl = httpServletRequest.getParameter("localUrl");
            if(StringUtils.isEmpty(localUrl)){
                jsonResult.setMessage("请输入链接地址！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }
            localUrl = "/moduleaggregations/" + localUrl;
            featuredActivity.setLinkUrl(localUrl);
        } else{
            String outUrl = httpServletRequest.getParameter("outUrl");
            if(StringUtils.isEmpty(outUrl)){
                jsonResult.setMessage("请输入链接地址！");
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }
            featuredActivity.setLinkUrl(outUrl);
        }
        if(file == null && file.isEmpty()){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage("请选择图片文件");
            return jsonResult;
        }
        //时间转换
        try {
            if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)){
                String pattern="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
                featuredActivity.setActivityEnd(dateFormat.parse(end));
                featuredActivity.setActivityStart(dateFormat.parse(start));
            }
        }catch (Exception e){
            jsonResult.setMessage("时间设置错误");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }


        jsonResult = featuredActivityService.insertFeaturedActivity(featuredActivity, aid, file, jsonResult);

        return jsonResult;
    }


    /**
     * 下架精选内容
     * @param featuredActivityid
     * @return
     */
    @GetMapping(value = "/{featuredActivityid}/rejection")
    @ResponseBody
    public JsonResult stopFeaturedActivity(@PathVariable(value = "featuredActivityid") Integer featuredActivityid){
        JsonResult jsonResult = new JsonResult();

        jsonResult = featuredActivityService.stopFeaturedActivity(featuredActivityid, jsonResult);

        return jsonResult;
    }


    /**
     * 上架精选内容
     * @param featuredActivityid
     * @return
     */
    @GetMapping(value = "/{featuredActivityid}/acceptance")
    @ResponseBody
    public JsonResult startFeaturedActivity(@PathVariable(value = "featuredActivityid") Integer featuredActivityid){
        JsonResult jsonResult = new JsonResult();

        jsonResult = featuredActivityService.startFeaturedActivity(featuredActivityid, jsonResult);

        return jsonResult;
    }


    /**
     * 删除一个精选页，状态设置为3
     * @param featuredActivityid
     * @return
     */
    @DeleteMapping(value = "/{featuredActivityid}")
    @ResponseBody
    public JsonResult deleteFeaturedActivitysById(@PathVariable(value = "featuredActivityid") Integer featuredActivityid){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(featuredActivityid)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = featuredActivityService.deleteFeaturedActivitysById(featuredActivityid, jsonResult);

        return jsonResult;
    }

    @PutMapping(value = "/order")
    @ResponseBody
    public JsonResult changeFeaturedActivitysOrder(@RequestParam(value = "cur") Integer cur, @RequestParam(value = "prev") Integer prev){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(cur) || StringUtils.isEmpty(prev)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = featuredActivityService.changeFeaturedActivitysOrder(cur, prev, jsonResult);

        return  jsonResult;
    }

}
