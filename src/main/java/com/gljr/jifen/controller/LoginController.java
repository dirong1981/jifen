package com.gljr.jifen.controller;


import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin")
public class LoginController {


    @Autowired
    private AdminService adminService;

    /**
     * 用户登录界面的跳转控制
     * localhost:8080/admin/login
     * localhost:8080/admin/
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = {"/login","/"}, method = RequestMethod.GET)
    public ModelAndView loginPage(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/login");

        //如果是登录状态，直接跳转到index
        HttpSession httpSession = httpServletRequest.getSession();
        if(httpSession.getAttribute(GlobalConstants.SESSION_ADMIN) !=  null){
            mv.setViewName("/admin/index");
        }

        return mv;
    }


    /**
     * 用户登录控制
     * @param admin
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult login(@Valid Admin admin, BindingResult bindingResult, HttpServletRequest httpServletRequest){

        JsonResult jr = new JsonResult();

        /**
         * 采用valid插件来验证数据的有效性，验证方法放在pojo实体类里面
         */
        if(bindingResult.hasErrors()){
            jr.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jr;
        }


        List<Admin> admins = adminService.login(admin);

        if(admins.isEmpty()){
            jr.setErrorCode(GlobalConstants.USER_DOES_NOT_EXIST);
            jr.setMessage(GlobalConstants.USER_DOES_NOT_EXIST_STR);
            return jr;
        }

        if(admin.getaPassword().equals(admins.get(0).getaPassword())){
            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

            HttpSession httpSession = httpServletRequest.getSession();
            httpSession.setAttribute(GlobalConstants.SESSION_ADMIN, admins.get(0).getaName());
            httpSession.setAttribute(GlobalConstants.SESSION_ADMIN_ID, admins.get(0).getaId());

            Map map = new HashMap();
            map.put("admin",admin);

            jr.setItem(map);

            return jr;
        }else{
            jr.setErrorCode(GlobalConstants.USER_PASSWORD_ERROR);
            jr.setMessage(GlobalConstants.USER_PASSWORD_ERROR_STR);
            return jr;
        }

        //return map;
    }

    //用户登出
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView("/admin/login");

        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.invalidate();

        return  mv;
    }

}
