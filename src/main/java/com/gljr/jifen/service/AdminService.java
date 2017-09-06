package com.gljr.jifen.service;

import com.gljr.jifen.pojo.*;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Admin login(Admin admin);

    List<AdminPermissionAssign> getPermission(int id);

    int insertAdminOnline(AdminOnline adminOnline);

    List<AdminOnline> selectAdminOnlines(int aId, Byte clientType);

    int updataPwd(Admin admin);

    Admin getAdmin(Integer id);


    Admin getAdmin(String username);


    int addAdmin(Admin admin);

    List<AdminList> getListAdmin();

    int insertAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign);

    int updataAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign);

    int deleteProduct(Integer id);

}
