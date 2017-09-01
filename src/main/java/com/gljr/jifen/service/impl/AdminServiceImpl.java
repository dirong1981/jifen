package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.AdminMapper;
import com.gljr.jifen.dao.AdminOnlineMapper;
import com.gljr.jifen.dao.AdminPermissionAssignMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminPermissionAssignMapper adminPermissionAssignMapper;

    @Autowired
    private AdminOnlineMapper adminOnlineMapper;


    @Override
    public Admin login(Admin admin) {

        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        //System.out.printf(admin.getaName());
        criteria.andUsernameEqualTo(admin.getUsername());
        //criteria.andAPasswordEqualTo(admin.getaPassword());

        Admin selectAdmin = null;
        try {
            List<Admin> list = adminMapper.selectByExample(adminExample);
            selectAdmin = list.get(0);
        }catch (Exception e){
            System.out.println(e);
            //return GlobalConstants.USER_DOES_NOT_EXIST;
        }


        return selectAdmin;
    }

    @Override
    public List<AdminPermissionAssign> getPermission(int id) {

        AdminPermissionAssignExample adminPermissionAssignExample = new AdminPermissionAssignExample();
        AdminPermissionAssignExample.Criteria criteria = adminPermissionAssignExample.createCriteria();
        criteria.andAidEqualTo(id);

        List<AdminPermissionAssign> list = adminPermissionAssignMapper.selectByExample(adminPermissionAssignExample);

        return list;
    }

    @Override
    public int insertAdminOnline(AdminOnline adminOnline) {
        return adminOnlineMapper.insert(adminOnline);
    }

    @Override
    public List<AdminOnline> selectAdminOnlines(int aId, Byte clientType) {
        AdminOnlineExample adminOnlineExample = new AdminOnlineExample();
        AdminOnlineExample.Criteria criteria = adminOnlineExample.createCriteria();
        criteria.andAidEqualTo(aId);
        criteria.andClientTypeEqualTo(clientType);
        return adminOnlineMapper.selectByExample(adminOnlineExample);
    }


}
