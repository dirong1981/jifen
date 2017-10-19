package com.gljr.jifen.controller;

import com.gljr.jifen.common.*;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.vo.GouliUserInfo;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.DTChainService;
import com.gljr.jifen.service.RedisService;
import com.gljr.jifen.service.UserCreditsService;
import com.gljr.jifen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/users")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;


    @Autowired
    private UserCreditsService userCreditsService;

    @Autowired
    private DTChainService chainService;

    @Autowired
    private RedisService redisService;

    /**
     * 添加用户收货地址
     * @param userAddress 地址模型
     * @param bindingResult 验证类
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @PostMapping(value = "/addresses")
    @ResponseBody
    public JsonResult addAddress(@Valid UserAddress userAddress, BindingResult bindingResult, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        if(bindingResult.hasErrors()){
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            jsonResult.setMessage(GlobalConstants.NOTNULL);
            return jsonResult;
        }

        try{
            String uid = httpServletRequest.getHeader("uid");
            if(uid == null || uid.equals("")){
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else{

                //如果地址设置为默认，则取消之前的默认地址
                if(userAddress.getIsDefault() == 1){

                    UserAddress defaultUserAddress = userService.selectUserAddressByIsDefault(Integer.parseInt(uid));
                    if(defaultUserAddress != null) {
                        defaultUserAddress.setIsDefault(0);
                        userService.updateUserAddressById(defaultUserAddress);
                    }
                }

                userAddress.setUid(Integer.parseInt(uid));
                userAddress.setCreateTime(new Timestamp(System.currentTimeMillis()));
                userService.insertUserAddress(userAddress);



                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            }
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jsonResult;
    }


    /**
     * 查询用户所有收货地址
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/addresses")
    @ResponseBody
    public JsonResult getAddresses(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try{
            String uid = httpServletRequest.getHeader("uid");
            if(uid == null || uid.equals("")){
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else{

                List<UserAddress> userAddresses = userService.selectUserAddressByUid(Integer.parseInt(uid));

                Map map = new HashMap();
                map.put("data", userAddresses);

                jsonResult.setItem(map);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            }
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }


    /**
     * 获取用户默认地址，如果没有设置取出最新添加的地址
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/addresses/default")
    @ResponseBody
    public JsonResult getDefaultAddresses(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try{
            String uid = httpServletRequest.getHeader("uid");
            if(uid == null || uid.equals("")){
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else{

                UserAddress userAddress = userService.selectUserAddressByIsDefault(Integer.parseInt(uid));

                Map map = new HashMap();
                map.put("data", userAddress);

                jsonResult.setItem(map);

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            }
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }


    /**
     * 删除一个收货地址
     * @param id 地址id
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @DeleteMapping(value = "/addresses/{id}")
    @ResponseBody
    public JsonResult deleteAddress(@PathVariable(value = "id") Integer id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
            userService.deleteUserAddressById(id);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }

    @GetMapping("/recommend")
    @ResponseBody
    public HttpServletResponse toUserRecommend() {

        String uid = request.getHeader("uid");
        if (uid == null || uid.equals("")) {
            super.response.setStatus(401);
            return super.response;
        }

        GatewayResponse<Map<String, String>> response = this.chainService.userRecommend(Long.parseLong(uid));
        if (null == response || response.getCode() != 200
                || null == response.getContent() || response.getContent().isEmpty()) {
            super.response.setStatus(404);
            return super.response;
        }

        for(String h : response.getContent().keySet()) {
            super.response.setHeader(h, response.getContent().get(h));
        }
        super.response.setStatus(302);

        return null;
    }

    @GetMapping("/points")
    @ResponseBody
    public HttpServletResponse userEarnPoints() {

        String uid = request.getHeader("uid");
        if (uid == null || uid.equals("")) {
            super.response.setStatus(401);
            return super.response;
        }

        GatewayResponse<Map<String, String>> response = this.chainService.userEarnPoints(Long.parseLong(uid));
        if (null == response || response.getCode() != 200
                || null == response.getContent() || response.getContent().isEmpty()) {
            super.response.setStatus(404);
            return super.response;
        }

        for(String h : response.getContent().keySet()) {
            super.response.setHeader(h, response.getContent().get(h));
        }
        super.response.setStatus(302);

        return null;
    }


    /**
     * 根据id查询一个收货地址详情
     * @param id 地址id
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/addresses/{id}")
    @ResponseBody
    public JsonResult selectAddressById(@PathVariable(value = "id") Integer id, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try {
            UserAddress userAddress = userService.selectUserAddressById(id);

            Map map = new HashMap();
            map.put("data", userAddress);

            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setItem(map);
        }catch (Exception e){
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }

        return jsonResult;
    }


    /**
     * 修改收货地址
     * @param userAddress 地址模型
     * @param bindingResult 验证类
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/addresses/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateAddressById(@Valid UserAddress userAddress, BindingResult bindingResult, @PathVariable(value = "id") Integer id,
                                        HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
            String uid = httpServletRequest.getHeader("uid");
            if(uid == null || uid.equals("")){
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else {

                //判断修改的是不是用户自己的地址
                UserAddress selectAddress = userService.selectUserAddressById(id);
                String _uid = selectAddress.getUid()+"";
                if(_uid.equals(uid)) {

                    //如果地址设置为默认，则取消之前的默认地址
                    if (userAddress.getIsDefault() == 1) {

                        UserAddress defaultUserAddress = userService.selectUserAddressByIsDefault(Integer.parseInt(uid));
                        if (defaultUserAddress != null) {
                            defaultUserAddress.setIsDefault(0);
                            userService.updateUserAddressById(defaultUserAddress);
                        }
                    }

                    userAddress.setUid(Integer.parseInt(uid));
                    userAddress.setId(id);
                    userAddress.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    userService.updateUserAddressById(userAddress);
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                    jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
                }else{
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    jsonResult.setMessage(GlobalConstants.ILLEGAL_OPERATION);
                    return jsonResult;
                }
            }
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }


    /**
     * 获取一个用户的积分信息
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping(value = "/credits")
    @ResponseBody
    public JsonResult selectUserCreditsByUid(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try {
            String uid = httpServletRequest.getHeader("uid");
            if (uid == null || uid.equals("")) {
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            } else {
                UserCredits userCredits = this.userCreditsService.getUserCredits(Integer.parseInt(uid), DBConstants.OwnerType.CUSTOMER);
                if(userCredits == null){
                    jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    return jsonResult;
                }else{
                    Map map = new HashMap();
                    map.put("data", userCredits);

                    jsonResult.setItem(map);
                    jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                }
            }
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }


    @GetMapping("/pwCheck")
    @ResponseBody
    public JsonResult pwCheck(@RequestParam(value = "identify", required = false) String identify,
                              @RequestParam(value = "password", required = false) String password) {
        JsonResult jsonResult = new JsonResult();

        String uid = request.getHeader("uid");
        if (uid == null || uid.equals("")) {
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        if (StringUtils.isEmpty(identify) || !org.apache.commons.lang3.math.NumberUtils.isNumber(identify)) {
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        if (StringUtils.isEmpty(password)) {
            CommonResult.passwordError(jsonResult);
            return jsonResult;
        }

        GatewayResponse response = this.chainService.checkPassword(Long.parseLong(identify), password);
        if (null == response || response.getCode() != 200) {
            CommonResult.passwordError(jsonResult);
        } else {
            CommonResult.success(jsonResult);
        }

        return jsonResult;
    }




    /**
     * 获取够力用户信息
     * @param type 0登录，1验证密码，2获取用户信息
     * @param identify 用户名
     * @param password 密码
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */


    @GetMapping(value = "/verification")
    @ResponseBody

    public JsonResult connectGLJR(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "identify",required = false) String identify,
                                  @RequestParam(value = "password", required = false) String password,
                                  HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(type)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        //获取用户信息
        if(type.equals("2")){

            //uid不为空
            if(StringUtils.isEmpty(identify)){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }

            String realName;
            Integer totalValue;
            Integer validValue;
            String phone;
            Integer uid;

            try {
                //去够力那边取数据

                GatewayResponse<GouliUserInfo> gouliUserInfo = chainService.getUserInfo(identify);

                if (null == gouliUserInfo || gouliUserInfo.getCode() != 200) {
                    CommonResult.passwordError(jsonResult);
                } else {
                    CommonResult.success(jsonResult);
                }

                realName = gouliUserInfo.getContent().getRealName();
                totalValue = Integer.parseInt(String.valueOf(gouliUserInfo.getContent().getTotalValue()));
                validValue = Integer.parseInt(String.valueOf(gouliUserInfo.getContent().getValidValue()));
                phone = gouliUserInfo.getContent().getPhone();
                uid = Integer.parseInt(String.valueOf(gouliUserInfo.getContent().getId()));

                User user = new User();
                user.setCellPhone(phone);
                user.setIntegral(totalValue);
                user.setRealName(realName);
                user.setUid(uid+"");

                Map map = new HashMap();
                map.put("data", user);

                jsonResult.setItem(map);
                CommonResult.success(jsonResult);

            }catch (Exception e){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }
        }else if (type.equals("1")){
            //验证密码
            String uid = httpServletRequest.getHeader("uid");
            //uid为空
            if(StringUtils.isEmpty(uid)){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }
            try {
                GatewayResponse response = this.chainService.checkPassword(Long.parseLong(uid), password);
                if (null == response || response.getCode() != 200) {
                    CommonResult.passwordError(jsonResult);
                } else {
                    Integer random = (int)(Math.random()*100000000);
                    redisService.put(uid+"random", random+"", 60, TimeUnit.SECONDS);
                    jsonResult.setMessage(random+"");
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                }
            }catch (Exception e){
                CommonResult.userNotExit(jsonResult);
            }
        }else{
            CommonResult.noObject(jsonResult);
        }

        return jsonResult;

    }

    /**
     * 获取用户信息，
     * 1、如果token为空，返回用户在商城的个人信息，
     * 2、如果token不为空，去够力验证用户的token，添加或更新商城的登录状态，返回用户在商城的个人信息
     * @param uid 用户id
     * @param gltoken 够力token
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectUserInfo(@RequestParam(value = "uid", required = false) Integer uid, @RequestParam(value = "token", required = false) String gltoken){
        JsonResult jsonResult = new JsonResult();


        if(StringUtils.isEmpty(uid) && StringUtils.isEmpty(gltoken)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        jsonResult = userService.selecteUserInfoByUid(uid, gltoken, jsonResult);

        return jsonResult;
    }


}
