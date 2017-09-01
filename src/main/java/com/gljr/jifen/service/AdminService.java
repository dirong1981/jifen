package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.AdminOnline;
import com.gljr.jifen.pojo.AdminPermissionAssign;
import com.gljr.jifen.pojo.AdminPermissionAssignExample;

import java.util.List;

public interface AdminService {

    Admin login(Admin admin);

    List<AdminPermissionAssign> getPermission(int id);

    int insertAdminOnline(AdminOnline adminOnline);

    List<AdminOnline> selectAdminOnlines(int aId, Byte clientType);



}
