package com.tmindtech.api.demoserver.bankcheck;

import com.alibaba.fastjson.JSONObject;
import com.fuqian.la.util.DESCoder;
import com.fuqian.la.util.HttpClient;
import com.fuqian.la.util.JSONConvertor;
import com.fuqian.la.util.MerchantSign;
import com.fuqian.la.util.RSAUtil;
import com.tmindtech.api.demoserver.example.service.HttpsClientRequestFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class BankCheckUtil {
    private static final String host = "http://test.fuqianla.net/dip";
    private static final String merchantId = "HZSC201805170036";
    private static final String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIh3kfDShl3dg25C1hrbdhU7rjddGDVRbzuWd2qvhmumjdfcTsbM+kX5jHnlelEtc8aUcqix6JxwDpaSc0P0LffUdIm4YMVQEo4zypc1TinluL8S87clflkKxR+zHyclY+TG7XzBTwKzCesrz9DKFU0IYflNv0dxSTq6aH3rJtJZAgMBAAECgYAZz+P+1RMtNfTiKiotMk2Hfp5ZQnaNvzV24+L6fvDB8+LobXdJCwRbr/PuKoWUTTCU4wc8+UmyLU/VEyZSOCFpcpbTHRMJQlDlykNwUVMlOE6cgXfO5ooArdAoa2GZYsF503cudb4dkohL2x44TXdJNxYU+hFUPOntySvKfq1D8QJBAOhWfTvA0cppi0dEyqyCMkuiV0DWtgR0ya7J75n0utXw6dVjufVbStvIRqMcrF9ek0uuKvrn6JaCqQeCj7PQ2D8CQQCWXYnjA3HiIsW6r2E2M1bG3Tl59v9Yh+H4bBDh3Bh05M0Tt3xQRur2HOpujZFh4h8hP/09z7kZGXN7rxYQg+9nAkB3Wq78+kILrIqbMxC27C9wQVZ3fZWF1oUhIKXQvjMY4qkNMSz/iUV8gHchJgK6/3fFohR8TpRidX7l18GAy2G7AkA8985p8sKha3H7AROMk4Jy/c5JSsI8VlRkkwTBX6m28/LHNNW+AxNQGWdgzhwNsU7n/3ciMfCeYbh2hQaQSb6vAkEAnrasZQ30jSOlh+KkIuDHG03M1X53oLgy/Fil/whcIERqSbZsSXXbLUgJtml2OJrJKqSvDW2t/BCZhDZ45oabEA==";
    private static final String desKey = "6787216571349756570797365311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247132156";
    private static final String userId = "1039";

    public static String getCrawlId(String bankCode) {
        String url = host + "/api/getCrawlId";
        String paramStr = "{\"userId\":\"%s\",\"bankCode\":\"%s\"}";
        paramStr = String.format(paramStr, userId, bankCode);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        try {
            String result = HttpClient.post(url, params, null, 100000, 100000);
            System.out.println("Result:" + result);
            String enResult = RSAUtil.deStr(result, priKey);
            System.out.println("EnResult:" + enResult);
            return enResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String loginPersonalBank(String crawlId, String bankCode, String IDCardNo, String custName, String accountNo, String cardId, String pwd, String verifyCode, String phoneNo) {
        String url = host + "/api/loginA";
        String paramStr = "{\"userId\":\"%s\",\"crawlId\":\"%s\",\"bankCode\":\"%s\",\"IDCardNo\":\"%s\","
                + "\"custName\":\"%s\",\"accountNo\":\"%s\",\"cardId\":\"%s\",\"pwd\":\"%s\",\"verifyCode\":\"%s\",\"phoneNo\":\"%s\"}";
        paramStr = String.format(paramStr, userId, crawlId, bankCode, IDCardNo, custName, accountNo, cardId, pwd, verifyCode, phoneNo);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        try {
            String result = HttpClient.post(url, params, null, 10000, 60000);
            System.out.println("Result:" + result);
            String enResult = RSAUtil.deStr(result, priKey);
            System.out.println("EnResult:" + enResult);
            return enResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String verifySMSMessage(String crawlId, String bankCode, String messageCode, String pwd) {
        String url = host + "/api/submitMessage";
        String paramStr = "{\"userId\":\"%s\",\"crawlId\":\"%s\",\"bankCode\":\"%s\",\"messageCode\":\"%s\",\"pwd\":\"%s\"}";
        paramStr = String.format(paramStr, userId, crawlId, bankCode, messageCode, pwd);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        try {
            String result = HttpClient.post(url, params, null, 10000, 10000);
            System.out.println("Result:" + result);
            String enResult = RSAUtil.deStr(result, priKey);
            System.out.println("EnResult:" + enResult);
            return enResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String verifyAuthBank(String bankCode, String IDCardNo, String custName, String cardId) {
        String url = host + "/api/authenticate";
        String paramStr = "{\"userId\":\"%s\",\"bankCode\":\"%s\",\"IDCardNo\":\"%s\",\"custName\":\"%s\",\"cardId\":\"%s\"}";
        paramStr = String.format(paramStr, userId, bankCode, IDCardNo, custName, cardId);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        try {
            String result = HttpClient.post(url, params, null, 100000, 100000);
            System.out.println("Result:" + result);
            String enResult = RSAUtil.deStr(result, priKey);
            System.out.println("EnResult:" + enResult);
            return enResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getBankPersonalRecode(String cardId, String idNo, String bankCode, String beginDate, String endDate) throws Exception {
        String url = host + "/merchantController/getBasInfoByID";


        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("merNo", merchantId);
        treeMap.put("userId", userId);
        treeMap.put("cardId", cardId);
        treeMap.put("idNo", idNo);
        treeMap.put("bankCode", bankCode);
        treeMap.put("beginDate", beginDate);
        treeMap.put("endDate", endDate);
        String requestData = MerchantSign.genDesData(treeMap, priKey, desKey);
        Map<String, String> params = new HashMap<>();
        params.put("merNo", merchantId);
        params.put("requestData", requestData);
        String result = HttpClient.post(url, params, null, 10000000, 100000000);
        //标准出参
        System.out.println(result);
        //以上部分已经调用完毕，以下为获取到结果后解密的示例
        Map<String, Object> jsonResult = JSONConvertor.jsonToMap(result);
        String content = DESCoder.desDecrypt((String) jsonResult.get("responseData"), desKey);
        System.out.println(content);
        return content;
    }

    public static String getCardIdBinByCardNo(String cardId) {
        String url = host + "/api/queryCardBin";
        String paramStr = "{\"userId\":\"%s\",\"cardId\":\"%s\"}";
        paramStr = String.format(paramStr, userId, cardId);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        try {
            String result = HttpClient.post(url, params, null, 100000, 100000);
            System.out.println("Result:" + result);
            String enResult = RSAUtil.deStr(result, priKey);
            System.out.println("EnResult:" + enResult);
            return enResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getSupportedBankList() {
        String url = host + "/api/bankList";
        String paramStr = "{\"userId\":\"%s\"}";
        paramStr = String.format(paramStr, userId);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        try {
            String result = HttpClient.post(url, params, null, 10000, 10000);
            System.out.println("Result:" + result);
            String enResult = RSAUtil.deStr(result, priKey);
            System.out.println("EnResult:" + enResult);
            return enResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getLoginHistory(String loginName, String bankCode) {
        String url = host + "/api/historyList";
        String paramStr = "{\"userId\":\"%s\",\"loginName\":\"%s\",\"bankCode\":\"%s\"}";
        paramStr = String.format(paramStr, userId, loginName, bankCode);
        System.out.println("paramStr:" + paramStr);
        String requestData = RSAUtil.enStr(paramStr, priKey);
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("data", requestData);
        try {
            String result = HttpClient.post(url, params, null, 10000, 60000);
            System.out.println("Result:" + result);
            String enResult = RSAUtil.deStr(result, priKey);
            System.out.println("EnResult:" + enResult);
            return enResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
//        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        getCardIdBinByCardNo("6214835892584523");
//        getSupportedBankList();
//        verifyAuthBank("gdb", "331081198604300023", "将舒佳", "4063661340573885");
//        getCrawlId("icbc");
//        loginPersonalBank("yzxu7swu1527076667081@HZSC201805170036", "icbc", "360281199105094712", "齐引飞", "6212261202043617746", "6212261202043617746", "Qq6781069", "7431", "18657171743");
//        verifySMSMessage("MSsxkDNz1526984418831@HZSC201805170036", "icbc", "592540", "Qq6781069");
        System.out.println(TimeUnit.MILLISECONDS.toSeconds(1000));
//        while (true) {
//            getLoginHistory("18657171743", "icbc");
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                break;
//            }
//        }
//        getBankPersonalRecode("6212261202043617746", "360281199105094712", "icbc", "2018-05-11", "2018-05-21");


//        getCrawlId("gdb");
//        loginPersonalBank("daAfi9tK1527077445793@HZSC201805170036", "gdb",
//                "331081198604300023", "蒋舒佳", "4063661340573885",
//                "4063661340573885", "qwertyuiop33", "7431", "13858185094");
//        verifySMSMessage("MSsxkDNz1526984418831@HZSC201805170036", "gdb", "592540", "qwertyuiop33");

//        while (true) {
//            getLoginHistory("4063661340573885", "gdb");
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                break;
//            }
//        }
//        getBankPersonalRecode("4063661340573885", "331081198604300023", "gdb", "2018-01-11", "2018-05-21");


//        getCrawlId("cmb");
//        loginPersonalBank("Xzj8WKwh1527076860103@HZSC201805170036", "cmb",
//                "331081198604300023", "蒋舒佳", "13858185094",
//                "6214835892584523", "160812", "7431", "13858185094");
//        verifySMSMessage("MSsxkDNz1526984418831@HZSC201805170036", "cmb", "592540", "160812");

//        while (true) {
//            getLoginHistory("13858185094", "cmb");
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                break;
//            }
//        }
//        getBankPersonalRecode("6214835892584523", "331081198604300023", "cmb", "2018-01-11", "2018-05-21");

//        JSONObject json;
//        try {
//            String cardUrl = UriComponentsBuilder.fromHttpUrl("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json")
//                    .queryParam("_input_charset", "utf-8")
//                    .queryParam("cardBinCheck", true)
//                    .queryParam("cardNo", "62148392584523").toUriString();
//            json = restTemplate.getForObject(cardUrl, JSONObject.class);
//        } catch (Exception ex) {
//            throw new RuntimeException("失败");
//        }
//        if (json == null) {
//            throw new RuntimeException("失败");
//        }
//        System.out.println(json.getString("cardType"));
    }
}
