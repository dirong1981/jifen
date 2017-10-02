package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.AdminMapper;
import com.gljr.jifen.dao.AdminOnlineMapper;
import com.gljr.jifen.dao.AdminPermissionAssignMapper;
import com.gljr.jifen.dao.SystemPermissionMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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

    @Autowired
    private SystemPermissionMapper systemPermissionMapper;



    @Override
    public List<Admin> login(Admin admin) {

        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andUsernameEqualTo(admin.getUsername());


        return adminMapper.selectByExample(adminExample);
    }

    @Override
    public List<AdminPermissionAssign> selectAdminPermissionAssignByAid(int id) {

        AdminPermissionAssignExample adminPermissionAssignExample = new AdminPermissionAssignExample();
        AdminPermissionAssignExample.Criteria criteria = adminPermissionAssignExample.createCriteria();
        criteria.andAidEqualTo(id);


        return adminPermissionAssignMapper.selectByExample(adminPermissionAssignExample);
    }

    @Override
    public int insertAdminOnline(AdminOnline adminOnline) {
        return adminOnlineMapper.insert(adminOnline);
    }

    @Override
    public List<AdminOnline> selectAdminOnlinesByAid(int aid, int clientType) {
        AdminOnlineExample adminOnlineExample = new AdminOnlineExample();
        AdminOnlineExample.Criteria criteria = adminOnlineExample.createCriteria();
        criteria.andAidEqualTo(aid);
        criteria.andClientTypeEqualTo(clientType);
        return adminOnlineMapper.selectByExample(adminOnlineExample);
    }

    @Override
    public List<AdminOnline> selectAdminOnlinesByAid(int aid) {
        AdminOnlineExample adminOnlineExample = new AdminOnlineExample();
        AdminOnlineExample.Criteria criteria = adminOnlineExample.createCriteria();
        criteria.andAidEqualTo(aid);
        return adminOnlineMapper.selectByExample(adminOnlineExample);
    }

    @Override
    public int updateAdminOnlinesById(AdminOnline adminOnline) {
        return adminOnlineMapper.updateByPrimaryKey(adminOnline);
    }


    @Override
    public List<Admin> selectAdminByUsername(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria  criteria = adminExample.or();
        criteria.andUsernameEqualTo(username);
        return adminMapper.selectByExample(adminExample);
    }

    @Override
    public List<SystemPermission> selectSystemPermission() {
        return systemPermissionMapper.selectByExample(null);
    }

    @Override
    public Admin selectAdminById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    /**
     * 插入Admin
     * @param admin
     * @return
     */
    @Override
    public int insertAdmin(Admin admin) {
        return adminMapper.insert(admin);
    }

    @Override
    @Transactional
    public int insertAdmin(Admin admin, String[] permissions) {
        adminMapper.insert(admin);

        if(permissions.length != 0) {
            for (String permission : permissions) {
                AdminPermissionAssign adminPermissionAssign = new AdminPermissionAssign();
                adminPermissionAssign.setAid(admin.getId());
                adminPermissionAssign.setAssignTime(new Timestamp(System.currentTimeMillis()));
                adminPermissionAssign.setPermissionCode(Integer.parseInt(permission));

                insertAdminPermissionAssign(adminPermissionAssign);
            }
        }
        return 0;
    }

    @Override
    public int insertAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign) {
        return adminPermissionAssignMapper.insert(adminPermissionAssign);
    }

    @Override
    public int deleteAdminById(Integer id) {
        return adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteAdmin() {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.or();
        criteria.andUsernameNotEqualTo("admin");
        return adminMapper.deleteByExample(adminExample);
    }

    @Override
    public int updateAdminById(Admin admin) {
        return adminMapper.updateByPrimaryKey(admin);
    }


//    /**
//     * null的话是不更新的 会根据id相应改的
//     * @param admin
//     * @return
//     */
//    @Override
//    public int updataPwd(Admin admin) {
//        //
//        return adminMapper.updateByPrimaryKeySelective(admin);
//
//    }
//
//    /**
//     * 获取到一个admin信息
//     * @param id
//     * @return
//     */
//    @Override
//    public Admin getAdmin(Integer id) {
//        return adminMapper.selectByPrimaryKey(id);
//    }
//
//
//
//
////
////
////    @Override
////    public int insertAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign) {
////        return adminPermissionAssignMapper.insert(adminPermissionAssign);
////    }
////
////    @Override
////    public int updataAdminPermissionAssign(AdminPermissionAssign adminPermissionAssign) {
////        return adminPermissionAssignMapper.updateByPrimaryKeySelective(adminPermissionAssign);
////    }
////
////    @Override
////    public int deleteProduct(Integer id) {
////
////        AdminPermissionAssignExample example = new AdminPermissionAssignExample();
////        AdminPermissionAssignExample.Criteria  criteria = example.or();
////        criteria.andAidEqualTo(id);
////        adminPermissionAssignMapper.deleteByExample(example);
////
////        try {
////            AdminOnlineExample exampleo = new AdminOnlineExample();
////            AdminOnlineExample.Criteria  criteriao = exampleo.or();
////            criteriao.andAidEqualTo(id);
////            adminOnlineMapper.deleteByExample(exampleo);
////
////        }catch (Exception e)
////        {
////            System.out.println("这个用户没登录过");
////        }
////
////        return adminMapper.deleteByPrimaryKey(id);
////    }
//
    /**
     * 获取到所有管理员
     * @return
     */
    @Override
    public List<Admin> selectAdminsByType(int type) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andAccountTypeEqualTo(type);
        criteria.andUsernameNotEqualTo("admin");
        return adminMapper.selectByExample(adminExample);
    }
//
//
//    /**
//     * 获取某个的权限
//     * @param aId 管理员id
//     * @return
//     */
//    @Override
//    public List<AdminPermissionAssign> getAdminPermissionAssign(Integer aId) {
//        AdminPermissionAssignExample adminPermissionAssignExample = new AdminPermissionAssignExample();
//        AdminPermissionAssignExample.Criteria criteria = adminPermissionAssignExample.or();
//        criteria.andAidEqualTo(aId);
//        return adminPermissionAssignMapper.selectByExample(adminPermissionAssignExample);
//    }
//
    /**
     * 通过code查询一个系统权限
     * @param code 权限code
     * @return
     */
    @Override
    public SystemPermission selectSystemPermission(int code) {
        SystemPermissionExample systemPermissionExample = new SystemPermissionExample();
        SystemPermissionExample.Criteria criteria = systemPermissionExample.or();
        criteria.andCodeEqualTo(code);
        return systemPermissionMapper.selectByExample(systemPermissionExample).get(0);
    }
//
//    /**
//     * 添加系统权限
//     * @param systemPermission
//     * @return
//     */
//    @Override
//    public int insertSystemPermission(SystemPermission systemPermission) {
//        return systemPermissionMapper.insert(systemPermission);
//    }
//
//    /**
//     * 查询所有系统权限
//     * @return
//     */
//    @Override
//    public List<SystemPermission> selectSystemPermission() {
//        return systemPermissionMapper.selectByExample(null);
//    }
//
//    /**
//     * 删除一个系统权限
//     * @param id
//     * @return
//     */
//    @Override
//    public int deleteSystemPermission(Integer id) {
//        return systemPermissionMapper.deleteByPrimaryKey(id);
//    }
//
//    /**
//     * 删除父权限下面的子权限
//     * @param code
//     * @return
//     */
//    @Override
//    public int deleteSonSystemPermission(Integer code) {
//        SystemPermissionExample systemPermissionExample = new SystemPermissionExample();
//        SystemPermissionExample.Criteria criteria = systemPermissionExample.or();
//        criteria.andParentCodeEqualTo(code);
//        return systemPermissionMapper.deleteByExample(systemPermissionExample);
//    }
//
//    /**
//     * 通过id查询一个系统权限
//     * @param id 权限id
//     * @return
//     */
//    @Override
//    public SystemPermission selectSystemPermissionById(Integer id) {
//        return systemPermissionMapper.selectByPrimaryKey(id);
//    }


}
