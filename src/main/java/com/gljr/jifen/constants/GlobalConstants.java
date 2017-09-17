package com.gljr.jifen.constants;

public class GlobalConstants {

    //设置token相关信息
    public static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";


    //设置返回的请求域名
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
    public static final String PAGE_NOT_FOUND = "404";

    //错误请求
    public static final String BAD_REQUEST = "400";

    //没有权限
    public static final String FORBIDDEN_CODE = "403";

    //没有登录
    public static final String NOT_LOGIN = "401";

    //Token过期
    public static final String TOKEN_EXPIRED = "405";



    //图片上传失败返回码
    public  static final String UPLOAD_PICTURE_FAILED = "300";
    public  static final String UPLOAD_PICTURE_FAILED_MESSAGE = "上传图片出错，请重新上传！";

    public static final String USER_DOES_NOT_EXIST = "1000";
    public static final String USER_DOES_NOT_EXIST_STR = "用户名不存在，请重试！";
    public static final String USER_EXIST_STR = "商户管理员用户名已存在，请更换！";

    public static final String USER_PASSWORD_ERROR = "1100";
    public static final String USER_PASSWORD_ERROR_STR = "用户密码错误，请重试！";

    public static final String USER_LOGIN_FAILED = "1200";
    public static final String USER_LOGIN_FAILED_STR = "登陆失败，请重试";


    public static final String SESSION_ADMIN = "session_admin";

    public static final String SESSION_ADMIN_ID = "session_admin_id";
}
