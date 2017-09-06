package com.gljr.jifen.controller;


import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.util.YukiUtil;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.Md5Util;
import com.gljr.jifen.common.StrUtil;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/jifen")
public class AdminController {


    @Autowired
    private AdminService adminService;

    @Autowired
    private Md5Util md5Util;

    @Autowired
    private StrUtil strUtil;


    //GET


    //POST

    /**
     * @PathVariable 接受get参数
     * 修改密码
     * 据说要避免多级嵌套........读起来也难读
     */
    @RequestMapping(value = "/admins/updatapwd", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updataPwd(@RequestParam("npwd") String npwd, @RequestParam("oldpwd") String oldpwd, HttpServletRequest httpServletRequest) {

        /*更改密码 貌似没有什么要变的*/
        JsonResult jr = new JsonResult();

        if ("".equals(npwd) || npwd == null || "".equals(oldpwd) || oldpwd == null) {
            jr.setErrorCode("113");
            jr.setMessage("不能留空...");
            return jr;
        }

        String id = httpServletRequest.getHeader("id");
        jr.setErrorCode("1000");
        jr.setMessage("按理来说你应该看不到这个的...");

        //取到用户的id
        if (id != null) {

            Integer aId = Integer.valueOf(id);


            Admin admin = adminService.getAdmin(aId);//登录的用户信息..话说这些东西不应该放在一个方便取的bend里面嘛?

            if (admin != null) {

                oldpwd = md5Util.md5(oldpwd + admin.getSalt());//

                if (oldpwd.equals(admin.getPassword())) {

                    admin.setPassword(md5Util.md5(npwd + admin.getSalt()));
                    admin.setId(aId);

                    try {
                        adminService.updataPwd(admin);
                        jr.setErrorCode("200");
                        jr.setMessage("成功修改密码!");
                    } catch (Exception e) {
                        jr.setErrorCode("500");
                        jr.setMessage("修改密码失败!");
                    }
                } else {
                    jr.setErrorCode(GlobalConstants.USER_PASSWORD_ERROR);
                    jr.setMessage(GlobalConstants.USER_PASSWORD_ERROR_STR);
                }
            } else {
                jr.setErrorCode("110");
                jr.setMessage("修改密码失败!");
            }
        } else {
            jr.setErrorCode("120");
            jr.setMessage("修改密码失败!");
        }

        return jr;
    }


    /**
     * 获取所有管理员
     */
    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    @ResponseBody
    public Map getAllAdmin(HttpServletRequest httpServletRequest) {


        JsonResult jsonResult = new JsonResult();


        try {
            List<AdminList> data = adminService.getListAdmin();


            for (int i = 0; i < data.size(); i++) {

                String roots = data.get(i).getPermissionCode();

                AdminList adminList = data.get(i);
                //adminList.setPermissionCode(YukiUtil.getStr(YukiUtil.roots,roots,","));

                data.remove(i);
                data.add(i, adminList);
            }

            Map map = new HashMap();
            map.put("data", data);


            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

            return map;
        } catch (Exception e) {
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return null;
    }


    /**
     * 添加用户
     *
     * @param admin
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/admins/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addAdminAjax(@Valid Admin admin, BindingResult bindingResult, HttpServletRequest httpServletRequest) {

        JsonResult jr = new JsonResult();

        String[] roots = httpServletRequest.getParameterValues("aRule");
        //权限的不过这里存的是int所以先不理
        String root = YukiUtil.getStr(roots, ",");

        if (bindingResult.hasErrors()) {
            jr.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jr;
        }

        try {

            String salt = strUtil.randomKey(32);
            admin.setPassword(md5Util.md5(admin.getPassword() + salt));
            admin.setCreateTime(new Date());
            admin.setSalt(salt);
            admin.setAccountType(new Byte("1"));
            admin.setStatus(new Byte("1"));

            adminService.addAdmin(admin);//主表添加完成

            //读id出来
            admin = adminService.getAdmin(admin.getUsername());


            //后面还有两个表

            AdminPermissionAssign adminPermissionAssign = new AdminPermissionAssign();
            adminPermissionAssign.setAid(admin.getId());
            adminPermissionAssign.setPermissionCode(0);
            adminPermissionAssign.setAssignTime(new Date());
            adminService.insertAdminPermissionAssign(adminPermissionAssign);


            jr.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jr.setMessage("添加成功!");
        } catch (Exception e) {
            jr.setErrorCode(GlobalConstants.OPERATION_FAILED);
//            jr.setMessage("添加失败!");
            jr.setMessage(e.toString());
        }

        return jr;
    }


    /**
     * 删除一个Admin
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/admins/delete/{id}")
    @ResponseBody
    @AuthPassport(permission_code = "0")
    public JsonResult deleteAdminAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        JsonResult jsonResult = new JsonResult();


        try {

            adminService.deleteProduct(Integer.parseInt(id));
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        } catch (Exception e) {
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    /**
     * 获取到对应的用户
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/admins/get/{id}")
    @ResponseBody
    @AuthPassport(permission_code = "0")
    public JsonResult getAdminAjax(@PathVariable("id") String id, HttpServletRequest httpServletRequest) {
        JsonResult jsonResult = new JsonResult();

        try {
            List<AdminList> admins = adminService.getListAdmin();
            for (AdminList admin : admins) {
                if (admin.getId() == Integer.parseInt(id)) {
                    Map map = new HashMap();
                    map.put("admin", admin);
                    jsonResult.setItem(map);
                    break;
                }
            }
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);

        } catch (Exception e) {
            System.out.println(e);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }


        return jsonResult;
    }


    /**
     * 更改一个用户信息
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/admins/updata", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateAdminAjax(@Valid Admin admin, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        JsonResult jsonResult = new JsonResult();

        /**
         * 采用valid插件来验证数据的有效性，验证方法放在pojo实体类里面
         */
        if (bindingResult.hasErrors()) {
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            return jsonResult;
        }

        Admin oladmin = adminService.getAdmin(admin.getUsername());

        if (oladmin != null) {

            oladmin.setPassword(md5Util.md5(admin.getPassword() + admin.getSalt()));

            try {
                adminService.updataPwd(oladmin);
                jsonResult.setErrorCode("200");
                jsonResult.setMessage("修改成功!");
            } catch (Exception e) {
                jsonResult.setErrorCode("500");
                jsonResult.setMessage("修改失败!");
            }
        } else {
            jsonResult.setErrorCode(GlobalConstants.USER_PASSWORD_ERROR);
            jsonResult.setMessage(GlobalConstants.USER_PASSWORD_ERROR_STR);
        }

        return jsonResult;
    }

}
