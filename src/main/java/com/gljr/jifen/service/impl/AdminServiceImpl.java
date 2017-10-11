package com.gljr.jifen.service.impl;

import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.AdminMapper;
import com.gljr.jifen.dao.AdminOnlineMapper;
import com.gljr.jifen.dao.AdminPermissionAssignMapper;
import com.gljr.jifen.dao.SystemPermissionMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.RedisService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import sun.security.provider.PolicyParser;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    /**
     * redis token key前缀
     */
    private static final String REDIS_ADMIN_TOKEN_PREFIX = "admin_token_";
    /**
     * redis permission_codes key前缀
     */
    private static final String REDIS_PERMISSION_CODES_PREFIX = "permission_codes_";

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminPermissionAssignMapper adminPermissionAssignMapper;

    @Autowired
    private AdminOnlineMapper adminOnlineMapper;

    @Autowired
    private SystemPermissionMapper systemPermissionMapper;
    @Autowired
    private RedisService redisService;


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
        } catch (Exception e) {
            System.out.println(e);
            //return GlobalConstants.USER_DOES_NOT_EXIST;
        }


        return selectAdmin;
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
    public JsonResult selectSystemPermission(JsonResult jsonResult) {
        try {
            SystemPermissionExample systemPermissionExample = new SystemPermissionExample();
            SystemPermissionExample.Criteria criteria = systemPermissionExample.or();
            criteria.andParentCodeEqualTo(0);
            systemPermissionExample.setOrderByClause("id asc");

            Map map = new HashMap();

            List<SystemPermission> systemPermissions = systemPermissionMapper.selectByExample(systemPermissionExample);

            map.put("parent", systemPermissions);

            for(SystemPermission systemPermission : systemPermissions){
                SystemPermissionExample systemPermissionExample1 = new SystemPermissionExample();
                SystemPermissionExample.Criteria criteria1 = systemPermissionExample1.or();
                criteria1.andParentCodeEqualTo(systemPermission.getCode());
                systemPermissionExample1.setOrderByClause("id asc");
                List <SystemPermission> systemPermissions1 = systemPermissionMapper.selectByExample(systemPermissionExample1);

                map.put(systemPermission.getCode(), systemPermissions1);

                for (SystemPermission systemPermission1 : systemPermissions1){
                    SystemPermissionExample systemPermissionExample2 = new SystemPermissionExample();
                    SystemPermissionExample.Criteria criteria2 = systemPermissionExample2.or();
                    criteria2.andParentCodeEqualTo(systemPermission1.getCode());
                    systemPermissionExample2.setOrderByClause("id desc");
                    List<SystemPermission> systemPermissions2 = systemPermissionMapper.selectByExample(systemPermissionExample2);

                    map.put(systemPermission1.getCode(), systemPermissions2);
                }
            }



            CommonResult.success(jsonResult);
            jsonResult.setItem(map);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
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


    @Override
    public List<AdminOnline> selectAdminOnlines(int aId) {
        AdminOnlineExample adminOnlineExample = new AdminOnlineExample();
        AdminOnlineExample.Criteria criteria = adminOnlineExample.createCriteria();
        criteria.andAidEqualTo(aId);
        return adminOnlineMapper.selectByExample(adminOnlineExample);
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

    @Override
    public JsonResult doLogin(String username, String password, Integer type) {
        JsonResult jsonResult = new JsonResult();
        Admin admin = login(new Admin(username));
        if (admin != null) {
            try {
                String md5Password = Md5Util.md5(password + admin.getSalt());
                if (md5Password != null && md5Password.equals(admin.getPassword())) {
                    //生成32位token的key
                    String key = StrUtil.randomKey(32);

                    //生成token中的数据
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", admin.getId());
                    jsonObject.put("role", "STORE_ADMIN");
                    jsonObject.put("username", admin.getUsername());

                    String token;

                    //查询online表中用户是否存在
                    List<AdminOnline> list = selectAdminOnlinesByAid(admin.getId(), type);
                    if (list.size() == 0) {
                        //如果在线表中用户不存在，将用户存入online表
                        AdminOnline adminOnline = new AdminOnline();
                        adminOnline.setAid(admin.getId());
                        adminOnline.setToken(key);
                        adminOnline.setClientType(type);
                        adminOnline.setLoginTime(new Date());

                        insertAdminOnline(adminOnline);
                        //生成token
                        token = JwtUtil.createJWT(GlobalConstants.GLJR_PREFIX, jsonObject.toString(), key, GlobalConstants.TOKEN_FAILURE_TIME);
                    } else {
                        //用户存在
                        //更新token的时间
                        key = list.get(0).getToken();
                        token = JwtUtil.createJWT(GlobalConstants.GLJR_PREFIX, jsonObject.toString(), key, GlobalConstants.TOKEN_FAILURE_TIME);
                    }

                    admin.setToken(token);

                    //查询用户权限
                    String permission_codes = "";

                    this.redisService.put(REDIS_ADMIN_TOKEN_PREFIX + admin.getId(), token);
                    this.redisService.put(REDIS_PERMISSION_CODES_PREFIX + admin.getId(), permission_codes);

                    Map<Object, Object> mapData = new HashMap<>();
                    mapData.put("data", admin);

                    jsonResult.setItem(mapData);
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                    jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
                } else {
                    jsonResult.setErrorCodeAndMessage(GlobalConstants.STORE_USER_PASSWORD_ERROR);
                }
            } catch (Exception e) {
                jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            }
        } else {
            jsonResult.setErrorCodeAndMessage(GlobalConstants.USER_NOT_EXIST);
        }

        return jsonResult;
    }

    @Override
    public JsonResult doLogout(String uid, DBConstants.AdminAccountType accountType) {
        JsonResult jsonResult = new JsonResult();

        this.redisService.evict(REDIS_ADMIN_TOKEN_PREFIX + uid, REDIS_PERMISSION_CODES_PREFIX + uid);

        jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);

        return jsonResult;
    }

    @Override
    public boolean checkToken(String uid, String jwt) {
        int requestUid = NumberUtils.getInt(uid);
        if (requestUid == 0) {
            return false;
        }
        //从redis获取tonken，如果不存在再从数据库获取
        String storeToken = this.redisService.get("token" + uid);
        if (StringUtils.isBlank(storeToken)) {
            List<AdminOnline> adminOnlineList = selectAdminOnlines(requestUid);
            if (adminOnlineList.isEmpty()) {
                return false;
            }
            storeToken = adminOnlineList.get(0).getToken();
        }

        try {
            Claims claims = JwtUtil.parseJWT(jwt, storeToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
