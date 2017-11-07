package com.gljr.jifen.constants;

public class DBConstants {

    public final static String CACHE_USER_PAYMENT_CODE_KEY = "7d82-458e-a32f-3a29be3e96b0@";

    /**
     * 是否类型
     */
    public enum YesOrNo {
        NO(0, "No"), Yes(1, "Yes");
        private int code;

        private String description;

        YesOrNo(int code, String description) {
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

    /**
     * 管理员类型
     */
    public enum AdminAccountType {
        SYS_ADMIN(1, "系统管理员"), STORE_ADMIN(2, "商户管理员");

        private int code;

        private String description;

        AdminAccountType(int code, String description) {
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

    /**
     * 管理员账号状态
     */
    public enum AdminAccountStatus {
        INACTIVE(0, "未激活"), ACTIVED(1, "已激活"), DISABLED(2, "已禁用");


        private int code;

        private String description;

        AdminAccountStatus(int code, String description) {
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

    /**
     * 客户端类型
     */
    public enum ClientType {
        APP(1, "APP"), WEB(2, "WEB");

        private int code;

        private String description;

        ClientType(int code, String description) {
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

    /**
     * 商户状态
     */
    public enum MerchantStatus {
        INACTIVE(0, "未激活"), ACTIVED(1, "已激活"), OFFLINE(2, "已下线"), DELETED(-1, "已删除");
        private int code;

        private String description;

        MerchantStatus(int code, String description) {
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

    /**
     * 分类状态
     */
    public enum CategoryStatus {

        INACTIVE(0, "未激活"), ACTIVED(1, "已激活");
        private int code;

        private String description;

        CategoryStatus(int code, String description) {
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


    /**
     * 分类类别
     */
    public enum CategoryType {

        PRODUCT(1, "商品"), STORE(2, "商户");
        private int code;

        private String description;

        CategoryType(int code, String description) {
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


    public enum TrxType {
        OFFLINE(1, "线下交易"), ONLINE(2, "线上交易"), TRANSFER(3, "积分转赠"), REFUND(4, "交易退款");

        private int code;

        private String description;

        TrxType(int code, String description) {
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

    public enum OrderStatus {
        UNPAID(0, "待付款"), PAID(1, "已付款"), CANCELED(2, "已取消"), REFUND(3, "已退款"), SETTLED(4, "已结算"), DELETED(-1, "已删除");
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


    public enum TrxStatus {
        UNPAID(0, "未付款"), COMPLETED(1, "交易完成"), REFUND(2, "已退款"), SETTLED(3, "已结算"), CANCELED(4, "已取消");
        private int code;

        private String description;

        TrxStatus(int code, String description) {
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

    public enum OwnerType {
        CUSTOMER(1, "用户"), MERCHANT(2, "商户");
        private int code;

        private String description;

        OwnerType(int code, String description) {
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

    public enum OnlineProductType {
        NORMAL_PRODUCT(1, "常规商品"), SYSTEM_VIRTUAL_PRODUCT(2, "平台虚拟商品"), STORE_COUPON(3, "商户代金券");
        private int code;

        private String description;

        OnlineProductType(int code, String description) {
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

    public enum ProductStatus {
        DRAFT(0, "草稿"), ON_SALE(1, "已上架"), SOLD_OUT(2, "已售罄"), OFF_SALE(3, "已下架"), DELETED(-1, "已删除");
        private int code;

        private String description;

        ProductStatus(int code, String description) {
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

    public enum FeaturedActivityStatus {
        INACTIVE(0, "未开始"), ACTIVED(1, "活跃中"), OFFLINE(2, "已过期"), DELETED(-1, "已删除");
        private int code;

        private String description;

        FeaturedActivityStatus(int code, String description) {
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

    public enum ModuleStatus {
        INACTIVE(0, "未激活"), ACTIVED(1, "已激活"), DELETED(-1, "已删除");
        private int code;

        private String description;

        ModuleStatus(int code, String description) {
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

    public enum ModuleType {
        PICTURE(1, "图片"), PRODUCT(2, "产品"), PICTUREANDPRODUCT(3, "图片+产品"), PACKET(4, "加息券+红包");
        private int code;

        private String description;

        ModuleType(int code, String description) {
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

    public enum ModuleSecondType {
        PICTURE(1, "图片"), PRODUCT2(2, "2产品"), PRODUCT4(3, "4产品"), PRODUCT6(4, "6产品"), PRODUCT8(5, "8产品"), PACKET(6, "加息券+红包"), PICTURE2(7, "8产品"),
        PICTURE4(8, "8产品"), PICTURE6(9, "8产品"), PICTURE8(10, "8产品");
        private int code;

        private String description;

        ModuleSecondType(int code, String description) {
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


    /**
     * 聚合页状态
     */
    public enum ModuleAggregationStatus {

        INACTIVE(0, "下线"), ACTIVED(1, "上线"), DELETED(-1, "已删除");
        private int code;

        private String description;

        ModuleAggregationStatus(int code, String description) {
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


    /**
     * 聚合页查询内容
     */
    public enum ModuleAggregationType {

        PRODUCT(1, "商品"), STORE(2, "商户"), CONDITION(3, "条件");
        private int code;

        private String description;

        ModuleAggregationType(int code, String description) {
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


    public enum MerchantType {
        ONLINE(1, "线上商品商户"), OFFLINE(2, "线下扫码商户");
        private int code;

        private String description;

        MerchantType(int code, String description) {
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

    public enum StoreCouponStatus {
        INVALID(0, "未激活"), ACTIVED(1, "已激活"), SOLD_OUT(2, "已兑完"), DELETED(-1, "已删除");
        private int code;

        private String description;

        StoreCouponStatus(int code, String description) {
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

    public enum CouponStatus {
        VALID(1, "有效的"), USED(2, "已使用"), EXPIRED(3, "已过期"), REFUND(4, "已退款");

        private int code;

        private String description;

        CouponStatus(int code, String description) {
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

    public enum CouponValidityType {
        DATE_RANGE(1, "起止日期"), DAYS_LATER(2, "领取后多少天");

        private int code;

        private String description;

        CouponValidityType(int code, String description) {
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

    public enum SettleType {
        BANK_TRANSFER(1, "银行汇款");

        private int code;

        private String description;

        SettleType(int code, String description) {
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

    public enum SettleStatus {
        IN_PROGRESS(0, "结算中"), SETTLED(1, "结算中");

        private int code;

        private String description;

        SettleStatus(int code, String description) {
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
}


