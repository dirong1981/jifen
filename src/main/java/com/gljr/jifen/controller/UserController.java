package com.gljr.jifen.controller;

import com.gljr.jifen.common.HttpClientHelper;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.UserAddress;
import com.gljr.jifen.pojo.UserCredits;
import com.gljr.jifen.service.UserCreditsService;
import com.gljr.jifen.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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
@RequestMapping(value = "/v1")
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
                        defaultUserAddress.setIsDefault(new Byte("0"));
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

                //如果地址设置为默认，则取消之前的默认地址
                if(userAddress.getIsDefault() == 1){

                    UserAddress defaultUserAddress = userService.selectUserAddressByIsDefault(Integer.parseInt(uid));
                    if(defaultUserAddress != null) {
                        defaultUserAddress.setIsDefault(new Byte("0"));
                        userService.updateUserAddressById(defaultUserAddress);
                    }
                }

                userAddress.setUid(Integer.parseInt(uid));
                userAddress.setId(id);
                userAddress.setCreateTime(new Timestamp(System.currentTimeMillis()));
                userService.updateUserAddressById(userAddress);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
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
    @GetMapping(value = "/users/credits")
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

    @GetMapping(value = "/users")
    @ResponseBody

    public JsonResult connectGLJR(@RequestParam(value = "type") String type, @RequestParam(value = "identify",required = false) String identify,
                                    @RequestParam(value = "password", required = false) String password,
                                    HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){

        JsonResult jsonResult = new JsonResult();


        String uid = httpServletRequest.getHeader("uid");
        if(uid == null || uid.equals("")){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }else {
            int code = (int) (Math.random() * 10000);
            String url = "";
            String text = "";

            System.out.println(userInfo_url);

            //0登录，1验证密码，2获取信息
            if (type.equals("0")) {
                //登录
                text = identify + password + code;
//                url = login_url + "?identify=" + identify + "&password=" + password + "&code=" + code + "&checkValue=" + StrUtil.encryption4AnyCode(text, code + "");
                url = "http://120.27.166.31/pointsMall/user/login.html?identify=" + identify + "&password=" + password + "&code=" + code + "&checkValue=" + StrUtil.encryption4AnyCode(text, code + "");

            } else if (type.equals("1")) {
                //验证密码
                text = uid + password + code;
//                url = pwCheck_url + "?identify=" + uid + "&password=" + password + "&code=" + code + "&checkValue=" + StrUtil.encryption4AnyCode(text, code + "");
                url = "http://120.27.166.31/pointsMall/user/pwCheck.html?identify=" + uid + "&password=" + password + "&code=" + code + "&checkValue=" + StrUtil.encryption4AnyCode(text, code + "");

            } else if (type.equals("2")) {
                text = identify + code;
//                url = userInfo_url + "?identify=" + identify + "&code=" + code + "&checkValue=" + StrUtil.encryption4AnyCode(text, code + "");
                url = "http://120.27.166.31/pointsMall/user/userInfo.html?identify=" + identify + "&code=" + code + "&checkValue=" + StrUtil.encryption4AnyCode(text, code + "");


            } else {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                return jsonResult;
            }

            String result = HttpClientHelper.httpClientGet(url);
            JSONObject jsonObject = new JSONObject(result);

            Map map = new HashMap();

            if (jsonObject.getString("code").equals("200")) {
                try {
                    //jsonObject = new JSONObject(jsonObject.get("data"));
//            System.out.println(jsonObject.get("data"));
                    if (jsonObject.has("data")) {
                        jsonObject = (JSONObject) jsonObject.get("data");

                        //jsonObject = new JSONObject(data);
                        map.put("realName", jsonObject.getString("realName"));
                        map.put("id", jsonObject.getInt("id"));
                        map.put("phone", jsonObject.getString("phone"));
                        map.put("userName", jsonObject.getString("userName"));
                        map.put("totalValue", jsonObject.getInt("totalValue"));
                        map.put("validValue", jsonObject.getInt("validValue"));

                        Map map1 = new HashMap();
                        map1.put("data", map);

                        jsonResult.setItem(map1);


                        UserCredits userCredits = new UserCredits();
                        userCredits.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        userCredits.setFrozenIntegral(0);
                        userCredits.setIntegral(jsonObject.getInt("validValue"));
                        userCredits.setOwnerId(jsonObject.getInt("id"));
                        userCredits.setOwnerType(new Byte("1"));
                        userCredits.setWalletAddress("xxxxxx");

                        userCreditsService.insertUserCredits(userCredits);

                    }
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                    jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
                } catch (Exception e) {
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                }
            } else {
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                jsonResult.setMessage("密码错误，请重试！");
            }
        }






        return jsonResult;
    }


}
