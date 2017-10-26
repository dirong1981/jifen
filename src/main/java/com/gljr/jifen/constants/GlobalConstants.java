package com.gljr.jifen.constants;

public class GlobalConstants {

    //设置token相关信息
    public static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";


    //设置返回的请求域名
    public static final String WEBDOMAIN = "http://console.iep.dtchain.io";
//        public static final String WEBDOMAIN = "http://localhost";
    public static final String APPDOMAIN = "http://m.p.dtchain.io";


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
    public static final String NO_ACTIVATION_CODE = "501";
    public static final String IS_DISABLE = "账号已禁用！";
    public static final String NOTHING_SELECT = "请选择内容再提交！";
    public static final String SQL_FAILED = "数据库错误！";
    public static final String NO_SELECTED = "没有添加商品或商户！";
    public static final String GREAT_THAN = "添加的数量和限定值不符，请重试！";
    public static final String NOT_ALLOWED_OPERATION = "不允许该操作！";
    public static final String OBJ_IS_USED = "该对象正在被使用！";
    public static final String USER_NO_EXIST = "获取用户数据失败！";

    //商家端api新增信息返回--------start--------
    /**
     * token过期时间
     */
    public static final long TOKEN_FAILURE_TIME = 60 * 60 * 24 * 360;
    public static final String GLJR_PREFIX = "gljr";
    /**
     * 缓存数据过期时间
     */
    public static final int CACHE_DATA_FAILURE_TIME = 60 * 10;
    /**
     * 积分和人民币兑换比例
     */
    public static final double INTEGRAL_RMB_EXCHANGE_RATIO = 10;
    /**
     * 缓存积分校验结果key前缀
     */
    public static final String CHECK_INTEGRAL_RESULT_PREFIX = "checkIntegralResult";
    public static final String[] STORE_USER_OPERATION_SECCESS = {OPERATION_SUCCEED, OPERATION_SUCCEED_MESSAGE};
    public static final String[] STORE_USER_USERNAME_IS_BLANK = {"499", "用户名不能为空"};
    public static final String[] STORE_USER_PASSWORD_IS_BLANK = {"498", "密码不能为空"};
    public static final String[] USER_NOT_LOGIN = {"497", "用户未登录"};
    public static final String[] STORE_USER_NOT_EXIST = {"496", "商户信息不存在"};
    public static final String[] REQUEST_PARAMETER_ERROR = {"495", "请求参数异常"};
    public static final String[] ORDER_INFO_NOT_EXIST = {"494", "订单信息不存在"};
    public static final String[] MORE_THAN_REFUND_TIME = {"493", "订单已超过可退款时间"};
    public static final String[] GET_USER_INFO_FAIL = {"492", "获取用户信息失败"};
    public static final String[] ORDER_CAN_NOT_REFUND = {"491", "该订单不能退款"};
    public static final String[] STORE_USER_PASSWORD_ERROR = {"490", "用户密码错误"};
    public static final String[] CAN_NOT_GET_USER_CREDIT = {"489", "无法获取用户积分信息"};
    public static final String[] NO_RESULT = {"488", "暂无结果"};
    public static final String[] USER_INTEGRAL_NOT_ENOUGH = {"487", INTEGRAL_NOT_ENOUGH};
    public static final String[] ORDER_STATUS_EXCEPTION = {"486", "订单状态异常"};
    public static final String[] PASSWORD_NOT_CHECK = {"485", "密码未校验"};
    public static final String[] USER_NOT_EXIST = {"484", "用户信息不存在"};
    public static final String[] COUPON_NOT_EXIST = {"483", "代金券不存在"};
    public static final String[] COUPON_USED_OR_LOSS = {"482", "代金券已使用或者已过期"};
    public static final String[] CONFIGURATION_ERROR = {"998", "配置错误"};
    public static final String[] SYSTEM_EXCEPTION = {"999", "系统异常"};
    public static final String SESSION_STORE_USER = "session_store_user";
    //商家端api新增信息返回--------end--------

    //是否超过免密额度 0：未超过 1：超过
    public enum ExceedLimit {
        NO(0, "未超过免密额度"), YES(1, "已超过免密额度");
        private int code;

        private String description;

        ExceedLimit(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    //交易状态 0：待付款 1：已付款 2：取消
    public enum OrderStatus {
        UNPAID(0, "待付款"), PAID(1, "已付款"), CANCELED(2, "已取消");
        private int code;

        private String description;

        OrderStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    //是否校验过密码 0：没有 1：已校验（不超过免密额度默认为1）
    public enum PwCheck {
        NO(0, "未校验"), YES(1, "已校验");
        private int code;

        private String description;

        PwCheck(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    //线上商品商户管理员权限，商品添加，修改，删除，商户信息修改，线上线下订单查看
    public static final String ONLINE_STORE_ADMIN_PERMISSION = "#11#1110#111004#111005#12#1210#121001#1211#121101#";

    //线下扫码商户
    public static final String OFFLINE_STORE_ADMIN_PERMISSION = "#10#1010#101001#101002#101004#101005#12#1110#111004#111005#12#1210#121001#1211#121101#";

//    public static final String[] STORE_ADMIN_PERMISSION = {"10","1010","101001","101002","101004","101005","11",
//            "1110","111004","111005","12","1210","121001","1211","121101"};
}
