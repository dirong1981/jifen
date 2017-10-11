package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.pojo.*;

import java.util.List;
import java.util.Map;

public interface AdminService {

    /**
     * 用户登录，查询用户
     * @param admin
     * @return
     */
    Admin login(Admin admin);

    /**
     * 查询管理员拥有的权限
     * @param id
     * @return
     */
    List<AdminPermissionAssign> selectAdminPermissionAssignByAid(int id);

    /**
     * 插入管理员在线状态
     * @param adminOnline
     * @return
     */
    int insertAdminOnline(AdminOnline adminOnline);

    /**
     * 查询某个用户在某个客户端的在线状态
     * @param aid
     * @param client_type
     * @return
     */
    List<AdminOnline> selectAdminOnlinesByAid(int aid, int client_type);

    List<AdminOnline> selectAdminOnlinesByAid(int aid);

    /**
     * 更新管理员的在线状态
     * @param adminOnline
     * @return
     */
    int updateAdminOnlinesById(AdminOnline adminOnline);

    /**
     * 用过id查询管理员信息
     * @param id
     * @return
     */
    Admin selectAdminById(Integer id);

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    int insertAdmin(Admin admin);
    int insertAdmin(Admin admin, String[] permissions);


    /**
     * 添加一个管理员拥有的权限
     * @param adminPermissionAssign
     * @return
     */
    int insertAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign);

    /**
     * 通过id删除管理员
     * @param id
     * @return
     */
    int deleteAdminById(Integer id);
    int deleteAdmin();


    /**
     * 通过id更新管理员
     * @param admin
     * @return
     */
    int updateAdminById(Admin admin);


    /**
     * 通过用户名查询管理员
     * @param username
     * @return
     */
    List<Admin> selectAdminByUsername(String username);


    /**
     * 查询所有权限
     * @return
     */
    JsonResult selectSystemPermission(JsonResult jsonResult);

    List<SystemPermission> selectSystemPermission();



////    int updataPwd(Admin admin);
////
////    Admin getAdmin(Integer id);
////
////
//    List<Admin> selectAdminByUsername(String username);
//
//    Admin selectAdminById(Integer id);
//
//    int insertAdmin(Admin admin);
//
//    int deleteAdminById(Integer id);
//
//    int updateAdminById(Admin admin);
//
//
////
////    int insertAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign);
////
////    int updataAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign);
////
////    int deleteProduct(Integer id);
//
    List<Admin> selectAdminsByType(int type);
//
    SystemPermission selectSystemPermission(int code);
//
//    int insertSystemPermission(SystemPermission systemPermission);
//
//
//
//    int deleteSystemPermission(Integer id);
//
//    int deleteSonSystemPermission(Integer code);
//
//    SystemPermission selectSystemPermissionById(Integer id);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param accountType 用户类型
     * @return JsonResult
     */
    JsonResult doLogin(String username, String password, Integer type);

    /**
     * 用户退出登录
     * @param uid 管理员ID
     * @param accountType 用户类型
     * @return JsonResult
     */
    JsonResult doLogout(String uid, DBConstants.AdminAccountType accountType);

    boolean checkToken(String uid, String token);

    List<AdminOnline> selectAdminOnlines(int aId);

}
