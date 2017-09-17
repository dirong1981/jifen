package com.gljr.jifen.service;

import com.gljr.jifen.pojo.*;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Admin login(Admin admin);

    List<AdminPermissionAssign> getPermission(int id);

    int insertAdminOnline(AdminOnline adminOnline);

    List<AdminOnline> selectAdminOnlines(int aId, Byte clientType);

//    int updataPwd(Admin admin);
//
//    Admin getAdmin(Integer id);
//
//
    List<Admin> selectAdminByUsername(String username);

    Admin selectAdminById(Integer id);

    int insertAdmin(Admin admin);

    int deleteAdminById(Integer id);

    int updateAdminById(Admin admin);


//
//    int insertAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign);
//
//    int updataAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign);
//
//    int deleteProduct(Integer id);

    List<Admin> getAdmins(String type);

    List<AdminPermissionAssign> getAdminPermissionAssign(Integer aId);

    SystemPermission getSystemPermission(int code);

    int insertSystemPermission(SystemPermission systemPermission);

    List<SystemPermission> selectSystemPermission();

    int deleteSystemPermission(Integer id);

    int deleteSonSystemPermission(Integer code);

    SystemPermission selectSystemPermissionById(Integer id);

}
