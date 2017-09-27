package com.gljr.jifen.constants;

public class GlobalConstants {

    //设置token相关信息
    public static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";


    //设置返回的请求域名
//    public static final String DOMAIN = "http://console.iep.dtchain.io";
    public static final String DOMAIN = "http://localhost";


    //数据验证错误
    public static final String VALIDATION_ERROR_CODE = "600";

    //操作成功返回码
    public static final String OPERATION_SUCCEED = "200";
    public static final String OPERATION_SUCCEED_MESSAGE = "操作成功！";


    //操作失败返回码
    public static final String OPERATION_FAILED = "500";
    public static final String OPERATION_FAILED_MESSAGE = "操作失败，请重试！";

    public static final String NOTNULL = "请填写所有内容！";


    //找不到页面代码
    public static final String OBJECT_NOT_FOUND = "404";
    public static final String OBJECT_NOT_FOUND_MESSAGE = "您所访问的资源不存在";

    //错误请求
    public static final String BAD_REQUEST = "400";

    //没有权限
    public static final String FORBIDDEN_CODE = "403";

    //没有登录
    public static final String NOT_LOGIN_CODE = "401";

    //Token过期
    public static final String TOKEN_EXPIRED = "405";

    //积分不足
    public static final String INTEGRAL_NOT_ENOUGH = "积分不足！";

    //数据库错误
    public static final String DATABASE_FAILED = "数据库错误！";


    //图片上传失败返回码
    public  static final String UPLOAD_PICTURE_FAILED = "300";
    public  static final String UPLOAD_PICTURE_FAILED_MESSAGE = "上传图片出错，请重新上传！";

    public static final String ADMIN_DOES_NOT_EXIST = "用户名不存在，请重试！";
    public static final String STORE_ADMIN_EXIST = "商户管理员用户名已存在，请更换！";

    public static final String ADMIN_PASSWORD_ERROR = "密码错误，请重试！";

    public static final String ADMIN_LOGIN_FAILED = "登陆失败，请重试";

    public static final String AUTH_FAILED = "没有操作权限";

    public static final String ILLEGAL_OPERATION = "非法操作！";
    public static final String NO_ACTIVATION = "账号未激活！";
    public static final String IS_DISABLE = "账号已禁用！";


}
