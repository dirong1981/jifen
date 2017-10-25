package com.gljr.jifen.controller;

import com.gljr.jifen.common.CaptchaUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.service.PlateService;
import com.gljr.jifen.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/index")
public class PlateController {

    @Autowired
    private PlateService plateService;

    @Autowired
    private RedisService redisService;

    @GetMapping
    @ResponseBody
    public JsonResult index(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = plateService.generatePlates(jsonResult);

        return jsonResult;
    }

    @GetMapping(value = "/code")
    @ResponseBody
    public void getCode(@RequestParam(value = "uid") String uid, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{

//        String uid = httpServletRequest.getHeader("uid");

        int code = (int)(Math.random() * 10000);

        redisService.put("USER_CODE" + uid, code+"", 60, TimeUnit.SECONDS);

        CaptchaUtil captchaUtil = new CaptchaUtil(code, uid);

        captchaUtil.outputCaptcha(httpServletRequest,httpServletResponse);

    }

}
