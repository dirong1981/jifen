package com.gljr.jifen.service;

import com.gljr.jifen.common.HttpClientHelper;
import com.gljr.jifen.common.RandomUtil;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.security.SignatureUtil;
import com.gljr.jifen.common.dtchain.vo.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;

@Service
public class DTChainService {

    private final static Gson gson = new Gson();

    private final static Logger LOG = LoggerFactory.getLogger(DTChainService.class);

    private final static String METHOD_POST = "POST";

    private final static String METHOD_GET = "GET";

    @Value("${dtchain.network.access.ak}")
    private String accessKey;

    @Value("${dtchain.network.access.sk}")
    private String secretKey;

    @Value("${dtchain.api.prefix}")
    private String apiEndpoint;

    @Value("${dtchain.api.ext.gouli.user.login}")
    private String apiGLUserLogin;

    @Value("${dtchain.api.ext.gouli.user.pwCheck}")
    private String apiGLUserPwCheck;

    @Value("${dtchain.api.ext.gouli.user.userInfo}")
    private String apiGLUserInfo;

    @Value("${dtchain.api.ext.gouli.user.userId}")
    private String apiGLUserId;

    @Value("${dtchain.api.ext.gouli.user.recommend}")
    private String apiGLUserRecommend;

    @Value("${dtchain.api.ext.gouli.user.earnPoints}")
    private String apiGLUserEarnPoints;

    @Value("${dtchain.api.integral.transfer}")
    private String apiIntegralTransfer;

    @Value("${dtchain.api.store.accounts}")
    private String apiStoreAccount;

    @Value("${dtchain.api.orders}")
    private String apiOrders;

    @Value("${dtchain.api.orders.offline}")
    private String apiOfflineOrders;

    @Value("${dtchain.api.orders.refund}")
    private String apiRefundOrders;

    @Value("${dtchain.api.settle.integral.stat}")
    private String apiSettleIntegralStat;

    @Value("${dtchain.api.settle.integral.issued.history}")
    private String apiSettleIntegralIssuedHistory;

    @Value("${dtchain.api.settle.integral.recycled.history}")
    private String apiSettleIntegralRecycledHistory;

    @Value("${dtchain.api.store.coupons}")
    private String apiStoreCoupons;

    @Value("${dtchain.api.orders.coupon.refund}")
    private String apiCouponRefund;

    @Value("${dtchain.api.settle.unsettles}")
    private String apiIntegralUnsettles;

    @Value("${dtchain.api.settle.unsettles.calc}")
    private String apiIntegralSettleToCalc;

    @Value("${dtchain.api.settle.remit}")
    private String apiSettleRemit;

    @Value("${dtchain.api.log.enable}")
    private String apiLogEnabled;

    private TreeMap<String, Object> _buildParams() {
        TreeMap<String, Object> params = new TreeMap<>();
        params.put(SignatureUtil.HEADER_INVOKE_ACCESS_KEY, accessKey);
        params.put(SignatureUtil.HEADER_INVOKE_NONCE, RandomUtil.nextLong(8));
        params.put(SignatureUtil.HEADER_INVOKE_TIMESTAMP, System.currentTimeMillis() / 1000);

        return params;
    }

    private boolean isLogEnabled() {
        return !StringUtils.isEmpty(apiLogEnabled) && "1".equals(apiLogEnabled);
    }

    public GatewayResponse login(String cellphone, String password) {
        TreeMap<String, Object> params = _buildParams();
        params.put("identify", cellphone);
        params.put("password", password);
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiGLUserLogin), secretKey));

            if (isLogEnabled()) {
                System.out.println("login | params | " + gson.toJson(params));
            }

            GatewayResponse response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                    HttpClientHelper.doPostSSL(apiEndpoint + apiGLUserLogin, params)
                    : HttpClientHelper.doPost(apiEndpoint + apiGLUserLogin, params), GatewayResponse.class);

            if (isLogEnabled()) {
                System.out.println("login | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse checkPassword(Long userId, String password) {
        TreeMap<String, Object> params = _buildParams();
        params.put("identify", String.valueOf(userId));
        params.put("password", password);
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiGLUserPwCheck), secretKey));

            if (isLogEnabled()) {
                System.out.println("checkPassword | params | " + gson.toJson(params));
            }

            GatewayResponse response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                    HttpClientHelper.doPostSSL(apiEndpoint + apiGLUserPwCheck, params)
                    : HttpClientHelper.doPost(apiEndpoint + apiGLUserPwCheck, params), GatewayResponse.class);

            if (isLogEnabled()) {
                System.out.println("checkPassword | response | " + gson.toJson(response));
            }

            return response;

        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<GouliUserInfo> getUserInfo(String cellphone) {
        TreeMap<String, Object> params = _buildParams();
        String userInfoAPI = String.format(apiGLUserInfo, cellphone);
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_GET, userInfoAPI), secretKey));

            if (isLogEnabled()) {
                System.out.println("getUserInfo | params | " + gson.toJson(params));
            }

            GatewayResponse<GouliUserInfo> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + userInfoAPI, params)
                            : HttpClientHelper.doGet(apiEndpoint + userInfoAPI, params),
                    new TypeToken<GatewayResponse<GouliUserInfo>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("getUserInfo | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<String> userRecommend(Long userId) {
        TreeMap<String, Object> params = _buildParams();
        String userRecommendAPI = String.format(apiGLUserRecommend, userId);
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_GET, userRecommendAPI), secretKey));

            if (isLogEnabled()) {
                System.out.println("userRecommend | params | " + gson.toJson(params));
            }

            GatewayResponse<String> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + userRecommendAPI, params)
                            : HttpClientHelper.doGet(apiEndpoint + userRecommendAPI, params),
                    new TypeToken<GatewayResponse<String>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("userRecommend | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<String> userEarnPoints(Long userId) {
        TreeMap<String, Object> params = _buildParams();
        String userEarnPointsAPI = String.format(apiGLUserEarnPoints, userId);
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_GET, userEarnPointsAPI), secretKey));

            if (isLogEnabled()) {
                System.out.println("userEarnPoints | params | " + gson.toJson(params));
            }

            GatewayResponse<String> response = gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + userEarnPointsAPI, params)
                            : HttpClientHelper.doGet(apiEndpoint + userEarnPointsAPI, params),
                    new TypeToken<GatewayResponse<String>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("userEarnPoints | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<GouliUserId> getUserId(String token) {
        TreeMap<String, Object> params = _buildParams();
        String userIdAPI = String.format(apiGLUserId, token);
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_GET, userIdAPI), secretKey));

            if (isLogEnabled()) {
                System.out.println("getUserId | params | " + gson.toJson(params));
            }

            GatewayResponse<GouliUserId> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + userIdAPI, params)
                            : HttpClientHelper.doGet(apiEndpoint + userIdAPI, params),
                    new TypeToken<GatewayResponse<GouliUserId>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("getUserId | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<CommonOrderResponse> transferIntegral(Long fromUid, Long toUid, Integer integral) {
        TreeMap<String, Object> params = _buildParams();
        params.put("from_uid", fromUid);
        params.put("to_uid", toUid);
        params.put("integral", integral);

        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiIntegralTransfer), secretKey));

            if (isLogEnabled()) {
                System.out.println("transferIntegral | params | " + gson.toJson(params));
            }

            GatewayResponse<CommonOrderResponse> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiIntegralTransfer, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiIntegralTransfer, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("transferIntegral | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse initStoreAccount(Long storeId) {
        TreeMap<String, Object> params = _buildParams();
        params.put("store_id", storeId);

        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiStoreAccount), secretKey));

            if (isLogEnabled()) {
                System.out.println("initStoreAccount | params | " + gson.toJson(params));
            }

            GatewayResponse response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                    HttpClientHelper.doPostSSL(apiEndpoint + apiStoreAccount, params)
                    : HttpClientHelper.doPost(apiEndpoint + apiStoreAccount, params), GatewayResponse.class);

            if (isLogEnabled()) {
                System.out.println("initStoreAccount | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<CommonOrderResponse> postOrders(Long fromUid, Long productId, Integer productType, Integer integral, Integer quantity) {
        TreeMap<String, Object> params = _buildParams();
        params.put("buyer_id", fromUid);
        params.put("product_id", productId);
        params.put("product_type", productType);
        params.put("integral", integral);
        params.put("quantity", null == quantity ? 1 : quantity);

        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiOrders), secretKey));

            if (isLogEnabled()) {
                System.out.println("postOrders | params | " + gson.toJson(params));
            }

            GatewayResponse<CommonOrderResponse> response = gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiOrders, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiOrders, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("postOrders | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<CommonOrderResponse> postOfflineOrders(Long fromUid, Long storeId, Integer integral) {
        TreeMap<String, Object> params = _buildParams();
        params.put("buyer_id", fromUid);
        params.put("store_id", storeId);
        params.put("integral", integral);

        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiOfflineOrders), secretKey));

            if (isLogEnabled()) {
                System.out.println("postOfflineOrders | params | " + gson.toJson(params));
            }

            GatewayResponse<CommonOrderResponse> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiOfflineOrders, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiOfflineOrders, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("postOfflineOrders | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<CommonOrderResponse> refundOrders(Long storeId, String dtchainBlockId, Integer refundIntegral) {
        TreeMap<String, Object> params = _buildParams();
        params.put("store_id", storeId);
        params.put("block_id", dtchainBlockId);
        params.put("refund_integral", refundIntegral);

        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiRefundOrders), secretKey));

            if (isLogEnabled()) {
                System.out.println("refundOrders | params | " + gson.toJson(params));
            }

            GatewayResponse<CommonOrderResponse> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiRefundOrders, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiRefundOrders, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("refundOrders | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<IntegralStatInfo> getIntegralStatInfo() {
        TreeMap<String, Object> params = _buildParams();
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_GET, apiSettleIntegralStat), secretKey));

            if (isLogEnabled()) {
                System.out.println("getIntegralStatInfo | params | " + gson.toJson(params));
            }

            GatewayResponse<IntegralStatInfo> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + apiSettleIntegralStat, params)
                            : HttpClientHelper.doGet(apiEndpoint + apiSettleIntegralStat, params),
                    new TypeToken<GatewayResponse<IntegralStatInfo>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("getIntegralStatInfo | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<List<IntegralIssuedHistory>> getIntegralIssuedHistory() {
        TreeMap<String, Object> params = _buildParams();
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_GET, apiSettleIntegralIssuedHistory), secretKey));

            if (isLogEnabled()) {
                System.out.println("getIntegralIssuedHistory | params | " + gson.toJson(params));
            }

            GatewayResponse<List<IntegralIssuedHistory>> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + apiSettleIntegralIssuedHistory, params)
                            : HttpClientHelper.doGet(apiEndpoint + apiSettleIntegralIssuedHistory, params),
                    new TypeToken<GatewayResponse<List<IntegralIssuedHistory>>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("getIntegralIssuedHistory | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<List<IntegralIssuedHistory>> getIntegralRecycledHistory() {
        TreeMap<String, Object> params = _buildParams();
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_GET, apiSettleIntegralRecycledHistory), secretKey));

            if (isLogEnabled()) {
                System.out.println("getIntegralRecycledHistory | params | " + gson.toJson(params));
            }

            GatewayResponse<List<IntegralIssuedHistory>> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + apiSettleIntegralRecycledHistory, params)
                            : HttpClientHelper.doGet(apiEndpoint + apiSettleIntegralRecycledHistory, params),
                    new TypeToken<GatewayResponse<List<IntegralIssuedHistory>>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("getIntegralRecycledHistory | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<List<MerchantSettleInfo>> getUnsettledIntegral() {
        TreeMap<String, Object> params = _buildParams();
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_GET, apiIntegralUnsettles), secretKey));

            if (isLogEnabled()) {
                System.out.println("getUnsettledIntegral | params | " + gson.toJson(params));
            }

            GatewayResponse<List<MerchantSettleInfo>> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + apiIntegralUnsettles, params)
                            : HttpClientHelper.doGet(apiEndpoint + apiIntegralUnsettles, params),
                    new TypeToken<GatewayResponse<List<MerchantSettleInfo>>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("getUnsettledIntegral | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<SettlePeriodStat> getUnsettledPeriodStat(Long storeId, String settleTo, String blockId) {
        TreeMap<String, Object> params = _buildParams();
        String api = String.format(apiIntegralSettleToCalc, storeId);
        if (StringUtils.isEmpty(blockId)) {
            params.put("settle_to", settleTo);
        } else {
            params.put("block_id", blockId);
        }
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_GET, api), secretKey));

            if (isLogEnabled()) {
                System.out.println("getUnsettledPeriodStat | params | " + gson.toJson(params));
            }

            GatewayResponse<SettlePeriodStat> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + api, params)
                            : HttpClientHelper.doGet(apiEndpoint + api, params),
                    new TypeToken<GatewayResponse<SettlePeriodStat>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("getUnsettledPeriodStat | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<CommonOrderResponse> verifyCoupon(Long storeId, String couponCode) {
        TreeMap<String, Object> params = _buildParams();
        params.put("store_id", storeId);
        params.put("coupon_code", couponCode);

        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiStoreCoupons), secretKey));

            if (isLogEnabled()) {
                System.out.println("verifyCoupon | params | " + gson.toJson(params));
            }

            GatewayResponse<CommonOrderResponse> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiStoreCoupons, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiStoreCoupons, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("verifyCoupon | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse settleRemit(String blockId) {
        TreeMap<String, Object> params = _buildParams();
        String api = String.format(apiSettleRemit, blockId);

        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, api), secretKey));

            if (isLogEnabled()) {
                System.out.println("settleRemit | params | " + gson.toJson(params));
            }

            GatewayResponse response = gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                    HttpClientHelper.doPostSSL(apiEndpoint + api, params)
                    : HttpClientHelper.doPost(apiEndpoint + api, params), GatewayResponse.class);

            if (isLogEnabled()) {
                System.out.println("settleRemit | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    public GatewayResponse<CommonOrderResponse> couponRefund(Long storeId, String couponCode) {
        TreeMap<String, Object> params = _buildParams();
        params.put("store_id", storeId);
        params.put("coupon_code", couponCode);

        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiCouponRefund), secretKey));

            if (isLogEnabled()) {
                System.out.println("couponRefund | params | " + gson.toJson(params));
            }

            GatewayResponse<CommonOrderResponse> response =  gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiCouponRefund, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiCouponRefund, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());

            if (isLogEnabled()) {
                System.out.println("couponRefund | response | " + gson.toJson(response));
            }

            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }
}
