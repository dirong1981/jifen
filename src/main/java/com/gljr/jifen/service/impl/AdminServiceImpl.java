package com.gljr.jifen.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.*;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.dao.*;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.AdminService;
import com.gljr.jifen.service.RedisService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl extends BaseService implements AdminService {

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

    @Autowired
    private StoreInfoMapper storeInfoMapper;


    @Override
    public JsonResult login(Admin admin, String client_type, JsonResult jsonResult) {

        try {
            //通过username查询用户
            AdminExample adminExample = new AdminExample();
            AdminExample.Criteria criteria = adminExample.or();
            criteria.andUsernameEqualTo(admin.getUsername());

            List<Admin> admins = adminMapper.selectByExample(adminExample);

            if(ValidCheck.validList(admins)){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }

            Admin selectAdmin = admins.get(0);

            //密码验证通过
            if(Md5Util.md5(admin.getPassword() + selectAdmin.getSalt()).equals(selectAdmin.getPassword())){

                //未激活，让用户修改密码
                if(selectAdmin.getStatus() == 0){
                    jsonResult.setErrorCode(GlobalConstants.NO_ACTIVATION_CODE);
                    jsonResult.setMessage(GlobalConstants.NO_ACTIVATION);
                    Map map = new HashMap();
                    map.put("aid", selectAdmin.getId());
                    jsonResult.setItem(map);
                    return jsonResult;
                }

                //账户禁用
                if(selectAdmin.getStatus() == 2){
                    jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                    jsonResult.setMessage(GlobalConstants.IS_DISABLE);
                    return jsonResult;
                }

                //查询该用户的权限
                String permission = "#";
                //如果账号为admin，获取所有权限
                if(selectAdmin.getUsername().equals("admin")) {
                    List<SystemPermission> systemPermissions = systemPermissionMapper.selectByExample(null);
                    for (SystemPermission systemPermission : systemPermissions){
                        permission = permission + systemPermission.getCode() + "#";
                    }
                }else{
                    AdminPermissionAssignExample adminPermissionAssignExample = new AdminPermissionAssignExample();
                    AdminPermissionAssignExample.Criteria criteria1 = adminPermissionAssignExample.or();
                    criteria1.andAidEqualTo(selectAdmin.getId());

                    List<AdminPermissionAssign> adminPermissionAssigns = adminPermissionAssignMapper.selectByExample(adminPermissionAssignExample);
                    if (ValidCheck.validList(adminPermissionAssigns)){
                        jsonResult.setMessage(GlobalConstants.AUTH_FAILED);
                        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                        return jsonResult;
                    }

                    for (AdminPermissionAssign adminPermissionAssign : adminPermissionAssigns) {
                        permission = permission + adminPermissionAssign.getPermissionCode() + "#";
                    }
                }

                //生成32位token的key
                String key = StrUtil.randomKey(32);


                //生成token中的数据
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", selectAdmin.getId());
                jsonObject.put("username", selectAdmin.getUsername());
                jsonObject.put("permission", permission);

                //生成token
                String token = JwtUtil.createJWT(GlobalConstants.GLJR_PREFIX, jsonObject.toString(), key, GlobalConstants.TOKEN_FAILURE_TIME);

                //查询用户在线状态并更新

                AdminOnlineExample adminOnlineExample = new AdminOnlineExample();
                AdminOnlineExample.Criteria criteria1 = adminOnlineExample.or();
                criteria1.andAidEqualTo(selectAdmin.getId());
                criteria1.andClientTypeEqualTo(DBConstants.ClientType.WEB.getCode());

                List<AdminOnline> adminOnlines = adminOnlineMapper.selectByExample(adminOnlineExample);
                if (ValidCheck.validList(adminOnlines)) {
                    AdminOnline adminOnline = new AdminOnline();

                    adminOnline.setAid(selectAdmin.getId());
                    adminOnline.setClientType(DBConstants.ClientType.WEB.getCode());
                    adminOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));
                    adminOnline.setToken(key);

                    adminOnlineMapper.insert(adminOnline);


                } else {
                    AdminOnline adminOnline = adminOnlines.get(0);

                    adminOnline.setToken(key);
                    adminOnline.setLoginTime(new Timestamp(System.currentTimeMillis()));

                    adminOnlineMapper.updateByPrimaryKey(adminOnline);
                }

                Map map = new HashMap();
                map.put("aid", selectAdmin.getId()+"");
                map.put("username", selectAdmin.getUsername());
                map.put("token", token);
                map.put("permission", permission);
                map.put("accountType", selectAdmin.getAccountType()+"");
                map.put("tokenKey", key);

                //把用户信息存入jedis
                this.redisService.put("admin_" + selectAdmin.getId(), JsonUtil.toJson(map), 60*60*24, TimeUnit.SECONDS);

                CommonResult.success(jsonResult);
                jsonResult.setItem(map);


            }else{
                CommonResult.passwordError(jsonResult);
            }
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    public JsonResult selectAdminPermissionAssignByAid(int id, JsonResult jsonResult) {

        try {
            Admin admin = adminMapper.selectByPrimaryKey(id);

            AdminPermissionAssignExample adminPermissionAssignExample = new AdminPermissionAssignExample();
            AdminPermissionAssignExample.Criteria criteria = adminPermissionAssignExample.or();
            criteria.andAidEqualTo(id);

            List<AdminPermissionAssign> adminPermissionAssigns = adminPermissionAssignMapper.selectByExample(adminPermissionAssignExample);

            Map map = new HashMap();
            map.put("admin", admin);
            map.put("permission", adminPermissionAssigns);

            CommonResult.success(jsonResult);
            jsonResult.setItem(map);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
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

                map.put("son" + systemPermission.getCode(), systemPermissions1);

                for (SystemPermission systemPermission1 : systemPermissions1){
                    SystemPermissionExample systemPermissionExample2 = new SystemPermissionExample();
                    SystemPermissionExample.Criteria criteria2 = systemPermissionExample2.or();
                    criteria2.andParentCodeEqualTo(systemPermission1.getCode());
                    systemPermissionExample2.setOrderByClause("id asc");
                    List<SystemPermission> systemPermissions2 = systemPermissionMapper.selectByExample(systemPermissionExample2);

                    map.put("son" + systemPermission1.getCode(), systemPermissions2);
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

    @Override
    public JsonResult selectAllAdminByType(Integer type, Integer page, Integer per_page, JsonResult jsonResult) {
        try {
            AdminExample adminExample = new AdminExample();
            AdminExample.Criteria criteria = adminExample.or();
            criteria.andAccountTypeEqualTo(type);
            criteria.andUsernameNotEqualTo("admin");
            adminExample.setOrderByClause("id desc");

            PageHelper.startPage(page,per_page);
            List<Admin> admins = adminMapper.selectByExample(adminExample);
            PageInfo pageInfo = new PageInfo(admins);

            for(Admin admin: admins){

                String adminPermissions= "没有权限";
                //循环所有管理员，获取该管理员所有权限
                //如果是系统管理员，查询他的权限，如果是商户管理员查询商户的名字
                if(type == DBConstants.AdminAccountType.SYS_ADMIN.getCode()) {
                    AdminPermissionAssignExample adminPermissionAssignExample = new AdminPermissionAssignExample();
                    AdminPermissionAssignExample.Criteria criteria1 = adminPermissionAssignExample.or();
                    criteria1.andAidEqualTo(admin.getId());
                    adminPermissionAssignExample.setOrderByClause("id asc");

                    List<AdminPermissionAssign> adminPermissionAssigns = adminPermissionAssignMapper.selectByExample(adminPermissionAssignExample);
                    if(!ValidCheck.validList(adminPermissionAssigns)){
                        adminPermissions = "";
                        for (AdminPermissionAssign adminPermissionAssign : adminPermissionAssigns) {

                            //循环所有权限，获取该权限的名字
                            SystemPermissionExample systemPermissionExample = new SystemPermissionExample();
                            SystemPermissionExample.Criteria criteria2 = systemPermissionExample.or();
                            criteria2.andCodeEqualTo(adminPermissionAssign.getPermissionCode());

                            List<SystemPermission> systemPermissions = systemPermissionMapper.selectByExample(systemPermissionExample);
                            if(!ValidCheck.validList(systemPermissions)){
                                adminPermissions += systemPermissions.get(0).getName() + ",";
                            }
                        }
                    }
                    //把权限集合放入管理员模型中
                    admin.setPermission(adminPermissions);
                }else{
                    StoreInfoExample storeInfoExample = new StoreInfoExample();
                    StoreInfoExample.Criteria criteria1 = storeInfoExample.or();
                    criteria1.andAidEqualTo(admin.getId());

                    List<StoreInfo> storeInfos = storeInfoMapper.selectByExample(storeInfoExample);
                    admin.setPermission(storeInfos.get(0).getName());
                }

                //查询管理员最后登录时间
                AdminOnlineExample adminOnlineExample = new AdminOnlineExample();
                AdminOnlineExample.Criteria criteria1 = adminOnlineExample.or();
                criteria1.andAidEqualTo(admin.getId());

                List<AdminOnline> adminOnlines = adminOnlineMapper.selectByExample(adminOnlineExample);
                if(!ValidCheck.validList(adminOnlines)){
                    admin.setCreateTime(adminOnlines.get(0).getLoginTime());
                }
            }
            Map map = new HashMap();
            map.put("data", admins);

            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            jsonResult.setItem(map);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return jsonResult;
    }

    @Override
    public JsonResult updateAdminPasswordByAid(Integer aid, String oldpwd, String npwd, JsonResult jsonResult) {

        try {
            Admin admin = adminMapper.selectByPrimaryKey(aid);

            if(ValidCheck.validPojo(admin)){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }

            String password = Md5Util.md5(oldpwd + admin.getSalt());

            if(password.equals(admin.getPassword())){
                password = Md5Util.md5(npwd + admin.getSalt());

                admin.setStatus(DBConstants.AdminAccountStatus.ACTIVED.getCode());
                admin.setPassword(password);

                adminMapper.updateByPrimaryKey(admin);
                CommonResult.success(jsonResult);
            }else{
                CommonResult.passwordError(jsonResult);
                return jsonResult;
            }
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }

        return jsonResult;
    }

    @Override
    public JsonResult modifyAdminByAid(Integer aid, String permissions, String pwd, JsonResult jsonResult) {

        try {
            Admin admin = adminMapper.selectByPrimaryKey(aid);
            if(ValidCheck.validPojo(admin)){
                CommonResult.userNotExit(jsonResult);
                return jsonResult;
            }

            //修改用户密码
            if(!StringUtils.isEmpty(pwd)) {
                String password = Md5Util.md5(pwd + admin.getSalt());
                admin.setPassword(password);
                adminMapper.updateByPrimaryKey(admin);
            }

            AdminPermissionAssignExample adminPermissionAssignExample = new AdminPermissionAssignExample();
            AdminPermissionAssignExample.Criteria criteria = adminPermissionAssignExample.or();
            criteria.andAidEqualTo(aid);

            adminPermissionAssignMapper.deleteByExample(adminPermissionAssignExample);

            String[] _permissions = permissions.split(",");

            for (String permission : _permissions){
                AdminPermissionAssign adminPermissionAssign = new AdminPermissionAssign();
                adminPermissionAssign.setAid(aid);
                adminPermissionAssign.setPermissionCode(Integer.parseInt(permission));
                adminPermissionAssign.setAssignTime(new Date());

                adminPermissionAssignMapper.insert(adminPermissionAssign);
            }

            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }


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



    @Override
    public JsonResult doLogin(String username, String password, Integer type) {
        JsonResult jsonResult = new JsonResult();
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.or();
        criteria.andUsernameEqualTo(username);

        List<Admin> admins = adminMapper.selectByExample(adminExample);
        if (ValidCheck.validList(admins)) {
            CommonResult.userNotExit(jsonResult);
            return jsonResult;
        }

        try {
            Admin admin = admins.get(0);
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
                List<AdminOnline> adminOnlines = selectAdminOnlinesByAid(admin.getId(), type);
                if (ValidCheck.validList(adminOnlines)) {
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
                    key = adminOnlines.get(0).getToken();
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
            e.printStackTrace();
            jsonResult.setErrorCode(GlobalConstants.VALIDATION_ERROR_CODE);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
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

    @Override
    public JsonResult selectAdminInfo(String uid) {
        try {
            Admin admin = adminMapper.selectByPrimaryKey(Integer.parseInt(uid));
            Map map = new HashMap();
            map.put("name", admin.getUsername());

            AdminOnlineExample adminOnlineExample = new AdminOnlineExample();
            AdminOnlineExample.Criteria criteria = adminOnlineExample.or();
            criteria.andAidEqualTo(Integer.parseInt(uid));
            criteria.andClientTypeEqualTo(DBConstants.ClientType.WEB.getCode());
            List<AdminOnline> adminOnlines = adminOnlineMapper.selectByExample(adminOnlineExample);
            map.put("online", adminOnlines.get(0).getLoginTime());

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }
}
