package com.gljr.jifen.service;

import com.gljr.jifen.common.HttpClientHelper;
import com.gljr.jifen.common.RandomUtil;
import com.gljr.jifen.common.dtchain.GatewayResponse;
import com.gljr.jifen.common.dtchain.security.SignatureUtil;
import com.gljr.jifen.common.dtchain.vo.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    private TreeMap<String, Object> _buildParams() {
        TreeMap<String, Object> params = new TreeMap<>();
        params.put(SignatureUtil.HEADER_INVOKE_ACCESS_KEY, accessKey);
        params.put(SignatureUtil.HEADER_INVOKE_NONCE, RandomUtil.nextLong(8));
        params.put(SignatureUtil.HEADER_INVOKE_TIMESTAMP, System.currentTimeMillis() / 1000);

        return params;
    }

    public GatewayResponse login(String cellphone, String password) {
        TreeMap<String, Object> params = _buildParams();
        params.put("identify", cellphone);
        params.put("password", password);
        try {
            params.put(SignatureUtil.HEADER_INVOKE_SIGNATURE, SignatureUtil.sign(SignatureUtil.makeSignPlainText(params,
                    METHOD_POST, apiGLUserLogin), secretKey));
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                    HttpClientHelper.doPostSSL(apiEndpoint + apiGLUserLogin, params)
                    : HttpClientHelper.doPost(apiEndpoint + apiGLUserLogin, params), GatewayResponse.class);
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                    HttpClientHelper.doPostSSL(apiEndpoint + apiGLUserPwCheck, params)
                    : HttpClientHelper.doPost(apiEndpoint + apiGLUserPwCheck, params), GatewayResponse.class);
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + userInfoAPI, params)
                            : HttpClientHelper.doGet(apiEndpoint + userInfoAPI, params),
                    new TypeToken<GatewayResponse<GouliUserInfo>>(){}.getType());
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + userIdAPI, params)
                            : HttpClientHelper.doGet(apiEndpoint + userIdAPI, params),
                    new TypeToken<GatewayResponse<GouliUserId>>(){}.getType());
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiIntegralTransfer, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiIntegralTransfer, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                    HttpClientHelper.doPostSSL(apiEndpoint + apiStoreAccount, params)
                    : HttpClientHelper.doPost(apiEndpoint + apiStoreAccount, params), GatewayResponse.class);
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiOrders, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiOrders, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiOfflineOrders, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiOfflineOrders, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doPostSSL(apiEndpoint + apiRefundOrders, params)
                            : HttpClientHelper.doPost(apiEndpoint + apiRefundOrders, params),
                    new TypeToken<GatewayResponse<CommonOrderResponse>>(){}.getType());
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + apiSettleIntegralStat, params)
                            : HttpClientHelper.doGet(apiEndpoint + apiSettleIntegralStat, params),
                    new TypeToken<GatewayResponse<IntegralStatInfo>>(){}.getType());
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
            return gson.fromJson(HttpClientHelper.isHttpsRequst(apiEndpoint) ?
                            HttpClientHelper.doGetSSL(apiEndpoint + apiSettleIntegralIssuedHistory, params)
                            : HttpClientHelper.doGet(apiEndpoint + apiSettleIntegralIssuedHistory, params),
                    new TypeToken<GatewayResponse<List<IntegralIssuedHistory>>>(){}.getType());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }
}
