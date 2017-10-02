package com.gljr.jifen.controller;

import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.UserCreditsService;
import com.gljr.jifen.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private UserCreditsService userCreditsService;

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
                List<UserCredits> userCredits = userCreditsService.selectUserCreditsByUid(Integer.parseInt(uid));
                if(userCredits == null){
                    jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    return jsonResult;
                }else{
                    Map map = new HashMap();
                    map.put("data", userCredits.get(0));

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



    /**
     * 获取够力用户信息
     * @param type 0登录，1验证密码，2获取用户信息
     * @param identify 用户名
     * @param password 密码
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */

    @Value("${static.gljr.login.url}")
    private String login_url;

    @Value("${static.gljr.pwCheck.url}")
    private String pwCheck_url;

    @Value("${static.gljr.userInfo.url}")
    private String userInfo_url;

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
            String token_key = StrUtil.randomKey(32);

            try {
                //去够力那边取数据
                int code = (int) (Math.random() * 10000);
//            int code = 3361;
                String text = identify + code;
                String url = "http://120.27.166.31/pointsMall/user/userInfo.html?identify=" + identify + "&code=" + code + "&checkValue=" + StrUtil.encryption4AnyCode(text, code + "");

                String result = HttpClientHelper.httpClientGet(url);
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("code").equals("200")) {
                    jsonObject = (JSONObject) jsonObject.get("data");

                    realName = jsonObject.getString("realName");
                    totalValue = jsonObject.getInt("totalValue");
                    validValue = jsonObject.getInt("validValue");
                    phone = jsonObject.getString("phone");
                    uid = jsonObject.getInt("id");
                }else {
                    CommonResult.userNotExit(jsonResult);
                    return jsonResult;
                }
            }catch (Exception e){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }

            try {
                List<UserCredits> userCreditss = userCreditsService.selectUserCreditsByUid(uid);
                if(ValidCheck.validList(userCreditss)) {
                    //用户不存在

                    UserCredits userCredits = new UserCredits();
                    userCredits.setIntegral(validValue);
                    userCredits.setOwnerType(1);
                    userCredits.setWalletAddress("xxxxx");
                    userCredits.setOwnerId(uid);
                    userCredits.setFrozenIntegral(totalValue);
                    userCredits.setCreateTime(new Timestamp(System.currentTimeMillis()));

                    UserExtInfo userExtInfo = new UserExtInfo();
                    userExtInfo.setCellphone(phone);
                    userExtInfo.setViewType(1);
                    userExtInfo.setUid(uid);

                    UserOnline userOnline = new UserOnline();
                    userOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));
                    userOnline.setToken(token_key);
                    userOnline.setUid(uid);

                    userService.insertUserInfo(userCredits, userExtInfo, userOnline);

                }else{

                    //积分表数据存在，更新
                    UserCredits userCredits = userCreditss.get(0);
                    userCredits.setFrozenIntegral(totalValue);
                    userCredits.setIntegral(validValue);

                    userCreditsService.updateUserCreditsById(userCredits);


                    List<UserExtInfo> userExtInfos = userService.selectUserExtInfoByUid(uid);
                    //不存在新添加
                    if(ValidCheck.validList(userExtInfos)){
                        UserExtInfo userExtInfo = new UserExtInfo();
                        userExtInfo.setViewType(1);
                        userExtInfo.setCellphone(phone);
                        userExtInfo.setUid(uid);

                        userService.insertUserExtInfo(userExtInfo);
                    }

                }

                User user = new User();
                user.setCellPhone(phone);
                user.setIntegral(validValue);
                user.setWallet_address("xxxxx");
                user.setRealName(realName);
                user.setUid(uid+"");

                Map map = new HashMap();
                map.put("data", user);

                jsonResult.setItem(map);
                CommonResult.success(jsonResult);

            }catch (Exception e){
                CommonResult.sqlFailed(jsonResult);
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
                int code = (int) (Math.random() * 10000);
                String text = uid + password + code;
                String url = "http://120.27.166.31/pointsMall/user/pwCheck.html?identify=" + uid + "&password=" + password + "&code=" + code + "&checkValue=" + StrUtil.encryption4AnyCode(text, code + "");

                String result = HttpClientHelper.httpClientGet(url);
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("code").equals("200")) {
                    CommonResult.success(jsonResult);
                } else {
                    CommonResult.passwordError(jsonResult);
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
     * 用户进入商城主页，获取用户信息，如果用户信息已存在，更新用户积分，如果不存在，添加用户积分，用户信息，用户登录状态
     * 返回token，用户电话号码，用户积分，用户真实姓名，钱包地址
     * @param uid
     * @param gltoken
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectUserInfo(@RequestParam(value = "uid", required = false) String uid, @RequestParam(value = "token", required = false) String gltoken,
                                     HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();


        if(StringUtils.isEmpty(uid)){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        String realName;
        Integer totalValue;
        Integer validValue;
        String phone;
        String token_key = StrUtil.randomKey(32);

        try {
            //去够力那边取数据
            int code = (int) (Math.random() * 10000);
//            int code = 3361;
            String text = uid + code;
            String url = "http://120.27.166.31/pointsMall/user/userInfo.html?identify=" + uid + "&code=" + code + "&checkValue=" + StrUtil.encryption4AnyCode(text, code + "");

            String result = HttpClientHelper.httpClientGet(url);
            JSONObject jsonObject = new JSONObject(result);

            if (jsonObject.getString("code").equals("200")) {
                jsonObject = (JSONObject) jsonObject.get("data");

                realName = jsonObject.getString("realName");
                totalValue = jsonObject.getInt("totalValue");
                validValue = jsonObject.getInt("validValue");
                phone = jsonObject.getString("phone");
            }else {
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }
        }catch (Exception e){
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        try {
            List<UserCredits> userCreditss = userCreditsService.selectUserCreditsByUid(Integer.parseInt(uid));
            if(ValidCheck.validList(userCreditss)) {
                //用户不存在

                    UserCredits userCredits = new UserCredits();
                    userCredits.setIntegral(validValue);
                    userCredits.setOwnerType(1);
                    userCredits.setWalletAddress("xxxxx");
                    userCredits.setOwnerId(Integer.parseInt(uid));
                    userCredits.setFrozenIntegral(totalValue);
                    userCredits.setCreateTime(new Timestamp(System.currentTimeMillis()));

                    UserExtInfo userExtInfo = new UserExtInfo();
                    userExtInfo.setCellphone(phone);
                    userExtInfo.setViewType(1);
                    userExtInfo.setUid(Integer.parseInt(uid));

                    UserOnline userOnline = new UserOnline();
                    userOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));
                    userOnline.setToken(token_key);
                    userOnline.setUid(Integer.parseInt(uid));

                    userService.insertUserInfo(userCredits, userExtInfo, userOnline);

            }else{

                UserCredits userCredits = userCreditss.get(0);
                userCredits.setFrozenIntegral(totalValue);
                userCredits.setIntegral(validValue);

                List<UserOnline> userOnlines = userService.selectUserOnlineByUid(Integer.parseInt(uid));

                if(ValidCheck.validList(userOnlines)){
                    //不存在新添加
                    UserOnline userOnline = new UserOnline();
                    userOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));
                    userOnline.setToken(token_key);
                    userOnline.setUid(Integer.parseInt(uid));

                    userService.insertUserOnline(userOnline);
                }else{
                    //用户存在更新
                    UserOnline userOnline = userOnlines.get(0);
                    userOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));

                    userService.updateUserInfo(userCredits, userOnline);

                }

                List<UserExtInfo> userExtInfos = userService.selectUserExtInfoByUid(Integer.parseInt(uid));
                //不存在新添加
                if(ValidCheck.validList(userExtInfos)){
                    UserExtInfo userExtInfo = new UserExtInfo();
                    userExtInfo.setViewType(1);
                    userExtInfo.setCellphone(phone);
                    userExtInfo.setUid(Integer.parseInt(uid));

                    userService.insertUserExtInfo(userExtInfo);
                }

            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", uid);
            jsonObject.put("phone", phone);

            //生成token
            String token = JwtUtil.createJWT("gljr", jsonObject.toString(), token_key, 60 * 60 * 24 * 360);

            User user = new User();
            user.setCellPhone(phone);
            user.setIntegral(validValue);
            user.setToken(token);
            user.setWallet_address("xxxxx");
            user.setRealName(realName);
            user.setUid(uid);

            Map map = new HashMap();
            map.put("data", user);

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }


}
