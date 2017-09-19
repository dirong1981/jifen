package com.gljr.jifen.controller;

import com.gljr.jifen.common.HttpClientHelper;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.IntegralTransferOrder;
import com.gljr.jifen.pojo.UserAddress;
import com.gljr.jifen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private StrUtil strUtil;


    /**
     * 添加用户收货地址
     * @param userAddress 地址模型
     * @param bindingResult 验证类
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 状态码
     */
    @RequestMapping(value = "addresses", method = RequestMethod.POST)
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
    @RequestMapping(value = "addresses", method = RequestMethod.GET)
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
    @RequestMapping(value = "addresses/default", method = RequestMethod.GET)
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
    @RequestMapping(value = "addresses/{id}", method = RequestMethod.DELETE)
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
    @RequestMapping(value = "addresses/{id}", method = RequestMethod.GET)
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
    @RequestMapping(value = "addresses/{id}/update", method = RequestMethod.POST)
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
     * 获取够力用户信息
     * @param type 0登录，1验证密码，2获取用户信息
     * @param identify 用户名
     * @param password 密码
     * @param httpServletResponse
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "users", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult validPassword(@RequestParam(value = "type") String type, @RequestParam(value = "identify",required = false) String identify,
                                    @RequestParam(value = "password", required = false) String password,
                                    HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){

        JsonResult jsonResult = new JsonResult();

        int code = (int)(Math.random() * 10000);
        String url = "";
        String text = "";

        //0登录，1验证密码，2获取信息
        if(type.equals("0")){

            text = identify + password + code;
            url = "http://120.27.166.31/pointsMall/user/login.html?identify=" + identify + "&password=" + password + "&code=" + code + "&checkValue=" + strUtil.encryption4AnyCode(text,code+"");

        }else if (type.equals("1")){
            text = password + code;
            url = "http://120.27.166.31/pointsMall/user/pwCheck.html?password=" + password + "&code=" + code + "&checkValue=" + strUtil.encryption4AnyCode(text,code+"");


        }else if (type.equals("2")){
            text = identify + code;
            url = "http://120.27.166.31/pointsMall/user/userInfo.html?identify=" + identify + "&code=" + code + "&checkValue=" + strUtil.encryption4AnyCode(text,code+"");


        }else{
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            return jsonResult;
        }

        String result = HttpClientHelper.httpClientGet(url);

        jsonResult.setMessage(result);
        jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        return jsonResult;
    }


}
