package com.tmindtech.api.demoserver.bankcheck;

import com.alibaba.fastjson.JSONObject;
import com.fuqian.la.util.HttpClient;
import com.fuqian.la.util.RSAUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhoneCheckUtil {
    private static final String preLoginUrl = "http://cwpay.fuqian.la/dip/merchantOperator/asynLogin";
    private static final String submitSmsUrl = "http://cwpay.fuqian.la/dip/merchantOperator/asynSubmitSms";
    private static final String submitImgUrl = "http://cwpay.fuqian.la/dip/merchantOperator/asynSubmitImgCode";
    private static final String loginStatusUrl = "http://cwpay.fuqian.la/dip/merchantOperator/getAsynStatus";
    private static final String detailInfoUrl = "http://cwpay.fuqian.la/dip/merchantOperator/queryUserListForYifenqi";
    private static final String renfaInfoUrl = "http://cwpay.fuqian.la/dip/renfaApi/getInfo";
    private static final String shixinInfoUrl = "http://cwpay.fuqian.la/dip/shixinApi/getInfo";

    private static final String merchantId = "HZSC201805170036";
    private static final String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIh3kfDShl3dg25C1hrbdhU7rjddGDVRbzuWd2qvhmumjdfcTsbM+kX5jHnlelEtc8aUcqix6JxwDpaSc0P0LffUdIm4YMVQEo4zypc1TinluL8S87clflkKxR+zHyclY+TG7XzBTwKzCesrz9DKFU0IYflNv0dxSTq6aH3rJtJZAgMBAAECgYAZz+P+1RMtNfTiKiotMk2Hfp5ZQnaNvzV24+L6fvDB8+LobXdJCwRbr/PuKoWUTTCU4wc8+UmyLU/VEyZSOCFpcpbTHRMJQlDlykNwUVMlOE6cgXfO5ooArdAoa2GZYsF503cudb4dkohL2x44TXdJNxYU+hFUPOntySvKfq1D8QJBAOhWfTvA0cppi0dEyqyCMkuiV0DWtgR0ya7J75n0utXw6dVjufVbStvIRqMcrF9ek0uuKvrn6JaCqQeCj7PQ2D8CQQCWXYnjA3HiIsW6r2E2M1bG3Tl59v9Yh+H4bBDh3Bh05M0Tt3xQRur2HOpujZFh4h8hP/09z7kZGXN7rxYQg+9nAkB3Wq78+kILrIqbMxC27C9wQVZ3fZWF1oUhIKXQvjMY4qkNMSz/iUV8gHchJgK6/3fFohR8TpRidX7l18GAy2G7AkA8985p8sKha3H7AROMk4Jy/c5JSsI8VlRkkwTBX6m28/LHNNW+AxNQGWdgzhwNsU7n/3ciMfCeYbh2hQaQSb6vAkEAnrasZQ30jSOlh+KkIuDHG03M1X53oLgy/Fil/whcIERqSbZsSXXbLUgJtml2OJrJKqSvDW2t/BCZhDZ45oabEA==";
    private static final String desKey = "6787216571349756570797365311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247132156";
    private static final String userId = "1004";

    /**
     * 预登录
     *
     * @param loginName    手机号
     * @param identityCard 身份证号
     * @param realName     真实姓名
     * @param password     服务密码
     * @return 预登录结果
     */
    public static String loginPrepare(String loginName, String identityCard, String realName, String password) {
        String paramStr = "{\"userId\":\"%s\",\"loginName\":\"%s\",\"password\":\"%s\"}";
        paramStr = String.format(paramStr, userId, loginName, password);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        String result = execute(preLoginUrl, params);

        System.out.println(result);

        JSONObject object = JSONObject.parseObject(result);
        System.out.println(JSONObject.parseObject(object.getString("data")).getString("crawlId"));


        return object.getString("data");
    }

    public static String submitSMSMessage(String crawlId, String loginName, String msgCode) {
        String paramStr = "{\"crawlId\":\"%s\",\"userId\":\"%s\",\"loginName\":\"%s\",\"msgCode\":\"%s\",\"serverType\":\"%s\"}";
        paramStr = String.format(paramStr, crawlId, userId, loginName, msgCode, "0");
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        String result = execute(submitSmsUrl, params);

        return result;
    }

    public static String submitIMGMessage(String crawlId, String userId, String loginName, String imageCode) {
        String paramStr = "{\"crawlId\":\"%s\",\"userId\":\"%s\",\"loginName\":\"%s\",\"imgCode\":\"%s\",\"serverType\":\"%s\"}";
        paramStr = String.format(paramStr, crawlId, userId, loginName, imageCode, "0");
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        return execute(submitImgUrl, params);
    }

    public static String getLoginStatus(String loginName) {
        String paramStr = "{\"userId\":\"%s\",\"loginName\":\"%s\"}";
        paramStr = String.format(paramStr, userId, loginName);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        JSONObject object = JSONObject.parseObject(execute(loginStatusUrl, params));
        String result = JSONObject.parseObject(object.getString("data")).getString("resCode");
        return result;
    }

    public static String getDetailInfo(String phoneNo) {
        String paramStr = "{\"phoneNo\":\"%s\"}";
        paramStr = String.format(paramStr, phoneNo);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        String result = execute(detailInfoUrl, params);
        System.out.println(result);
        return result;
    }

    private static String execute(String url, Map<String, String> params) {
        try {
            String result = HttpClient.post(url, params, null, 10000, 10000);
            return RSAUtil.deStr(result, priKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String getRenfaInfo(String userName, String idNum, String idType) {
        String paramStr = "{\"userId\":\"%s\",\"userName\":\"%s\",\"idNum\":\"%s\",\"idType\":\"%s\",\"orderNo\":\"%s\"}";
        String orderNo = merchantId + UUID.randomUUID().toString();
        paramStr = String.format(paramStr, userId, userName, idNum, idType, orderNo);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        return execute(renfaInfoUrl, params);
    }

    private static String getShixinInfo(String userName, String idNum, String idType) {
        String paramStr = "{\"userId\":\"%s\",\"userName\":\"%s\",\"idNum\":\"%s\",\"idType\":\"%s\",\"orderNo\":\"%s\"}";
        String orderNo = merchantId + UUID.randomUUID().toString();
        paramStr = String.format(paramStr, userId, userName, idNum, idType, orderNo);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        return execute(shixinInfoUrl, params);
    }

    public static void main(String[] args) {
//        image_host:   http://test.fuqianla.net/dip/
//        loginPrepare("13858185094", "331081198604300023", "蒋舒佳", "678106");
//        submitSMSMessage("ZkKg8zYq1527476308533", "13858185094", "823015");
//        submitIMGMessage("ZkKg8zYq1527476308533", userId, "13858185094", "kvvcv3");
//        while (true) {
//            getLoginStatus("13858185094");
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//                break;
//            }
//        }
        getDetailInfo("13858185094");
    }

}
