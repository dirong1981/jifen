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
        INACTIVE(0, "未激活"), ACTIVED(1, "已激活"), OFFLINE(2, "已下线"), DELETED(3, "已删除");
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
        UNPAID(0, "待付款"), PAID(1, "已付款"), CANCELED(2, "已取消"), DELETED(-1, "已删除");
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
        UNPAID(0, "未付款"),COMPLETED(1, "交易完成"), REFUND(2, "已退款"), SETTLED(3, "已结算"), CANCELED(4, "已取消");
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
}
