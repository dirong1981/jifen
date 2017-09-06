package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.AdminMapper;
import com.gljr.jifen.dao.AdminOnlineMapper;
import com.gljr.jifen.dao.AdminPermissionAssignMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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


    /**
     * null的话是不更新的 会根据id相应改的
     * @param admin
     * @return
     */
    @Override
    public int updataPwd(Admin admin) {
        //
        return adminMapper.updateByPrimaryKeySelective(admin);

    }

    /**
     * 获取到一个admin信息
     * @param id
     * @return
     */
    @Override
    public Admin getAdmin(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }



    @Override
    public Admin getAdmin(String username) {
        AdminExample example = new AdminExample();
        AdminExample.Criteria  criteria = example.or();
        criteria.andUsernameEqualTo(username);
        return adminMapper.selectByExample(example).get(0);
    }

    /**
     * 插入Admin
     * @param admin
     * @return
     */
    @Override
    public int addAdmin(Admin admin) {
        return adminMapper.insert(admin);
    }


    /**
     * 返回admin 用户列表
     * @return
     */
    @Override
    public List<AdminList> getListAdmin() {
        return adminMapper.getListAdmin();
    }


    @Override
    public int insertAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign) {
        return adminPermissionAssignMapper.insert(adminPermissionAssign);
    }

    @Override
    public int updataAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign) {
        return adminPermissionAssignMapper.updateByPrimaryKeySelective(adminPermissionAssign);
    }

    @Override
    public int deleteProduct(Integer id) {

        AdminPermissionAssignExample example = new AdminPermissionAssignExample();
        AdminPermissionAssignExample.Criteria  criteria = example.or();
        criteria.andAidEqualTo(id);
        adminPermissionAssignMapper.deleteByExample(example);

        try {
            AdminOnlineExample exampleo = new AdminOnlineExample();
            AdminOnlineExample.Criteria  criteriao = exampleo.or();
            criteriao.andAidEqualTo(id);
            adminOnlineMapper.deleteByExample(exampleo);

        }catch (Exception e)
        {
            System.out.println("这个用户没登录过");
        }

        return adminMapper.deleteByPrimaryKey(id);
    }


}
