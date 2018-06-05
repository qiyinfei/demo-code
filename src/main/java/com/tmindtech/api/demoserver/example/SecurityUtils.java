/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.tmindtech.api.demoserver.example;

import com.tmindtech.api.demoserver.example.service.HttpsClientRequestFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class SecurityUtils {

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    public static String getSignString(String parameter, String httpMethod, String timeStamp) {
        String strToSign = null;
        try {
            if (null != parameter) {
                parameter = URLDecoder.decode(parameter, "UTF-8");
            }

            strToSign = buildCanonicalString(parameter, httpMethod, timeStamp);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return strToSign;
        }

        return strToSign;
    }

    private static String buildCanonicalString(String parameters, String httpMethod, String timeStamp) {
        StringBuilder builder = new StringBuilder();
        builder.append(httpMethod).append("&");

        try {
            builder.append(URLEncoder.encode("/", "utf-8")).append("&");
            if (null != timeStamp) {
                builder.append(URLEncoder.encode(timeStamp, "utf-8")).append("&");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != parameters) {
            String queryString = parameters;

            // 去除#后面部分
            int fragIdx = parameters.indexOf('#');
            if (fragIdx >= 0) {
                queryString = parameters.substring(0, fragIdx);
            }

            builder.append(buildqueryString(queryString));
        }

        return builder.toString();
    }

    protected static String buildqueryString(String queryString) {
        StringBuilder builder = new StringBuilder("");

        Map<String, String> params = new TreeMap<String, String>();

        if (null != queryString) {
            String[] splits = queryString.split("&");
            for (int i = 0; i < splits.length; i++) {
                String query = splits[i];
                String[] tmpSplit = query.split("=");
                if (tmpSplit.length >= 2) {
                    String name = tmpSplit[0];
                    String value = tmpSplit[1];
                    params.put(name, value);
                }
            }
        }

        if (params.size() > 0) {
            String[] names = params.keySet().toArray(new String[params.size()]);
            Arrays.sort(names);
            char separator = '&';
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                if (0 != i) {
                    builder.append(separator);
                }
                builder.append(name);
                String paramValue = params.get(name);
                if (paramValue != null && paramValue.length() > 0) {
                    builder.append("=").append(paramValue);
                }
            }
        }

        String cononiStr = "";
        try {
            cononiStr = URLEncoder.encode(builder.toString(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cononiStr;
    }

    protected static byte[] hmacsha256Signature(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return mac.doFinal(data);
        } catch (Exception e) {
            logger.error("Hmac signature failed, {}.", e.toString());
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(downloadSubmitQueryResult("18657171743", "齐引飞", "360281199105094712"));
    }

    public static String downloadSubmitQueryResult(String phone, String name, String idCard) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        RestTemplate httpsTemplate = new RestTemplate(new HttpsClientRequestFactory());
        httpsTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        long timestamp = Timestamp.from(Instant.now()).getTime();
        String sign = md5Signature("API_ZHONGW" + timestamp + "30dc15246256912e");
        try {

            String url = UriComponentsBuilder.fromHttpUrl("https://wctplat.baybird.cn/yunrenapi/tt/submit")
                    .queryParam("appId", "API_ZHONGW")
                    .queryParam("timestamp", timestamp)
                    .queryParam("sign", sign)
                    .queryParam("phone", phone)
                    .queryParam("name", name)
                    .queryParam("idcard", idCard).toUriString();

            String url1 = UriComponentsBuilder.fromHttpUrl("https://wctplat.baybird.cn/yunrenapi/tt/query")
                    .queryParam("appId", "API_ZHONGW")
                    .queryParam("timestamp", timestamp)
                    .queryParam("sign", sign)
                    .queryParam("idcard", idCard).toUriString();


            String submit = httpsTemplate.postForObject(url,null, String.class);
            String query = httpsTemplate.postForObject(url1, null, String.class);
            return submit + ":" + query;
        } catch (RuntimeException rx) {
            return null;
        }
    }

    public static String md5Signature(String message) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(message.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;

    }

    public static String signWithHmac(String strToSign, String accessKey) {

        byte[] crypto = hmacsha256Signature(strToSign.getBytes(), accessKey.getBytes());

        String signature = Base64.encodeBase64String(crypto).trim();

        return signature;
    }

    public static byte[] readKeyByteFromFile(String fileName) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        String line = "";
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith("-----")) {
                sb.append(line).append("\r");
            }
        }

        return Base64.decodeBase64(sb.toString());
    }

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String PKCS8_KEY_FILE = "pkcs8_private.pem";

    private static PrivateKey priKey;

    static {
        try {
            String path = SecurityUtils.class.getResource("/").getPath() + PKCS8_KEY_FILE;
            path = URLDecoder.decode(path, "utf-8");
            byte[] privateKey = readKeyByteFromFile(path);
            //取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //生成私钥
            priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            logger.error("Failed to get private key, {}", e.toString());
        }
    }

    public static String signWithRsa(String strToSign) {
        String base64 = null;

        try {
            byte[] data = strToSign.getBytes("UTF-8");

            //实例化Signature
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            //初始化Signature
            signature.initSign(priKey);
            //更新
            signature.update(data);
            byte[] sigResult = signature.sign();
            base64 = Base64.encodeBase64String(sigResult);

/*      // 重要：临时调试用，版本发布时一定要删除
      byte[] base64Decode = Base64.decodeBase64(base64);
      boolean bResult = verifyRsaSignature(strToSign.getBytes(), base64Decode);
      if (!bResult) {
        logger.error("Failed to decode with rsa.");
      }*/
        } catch (Exception e) {
            logger.error("Failed to sign with rsa, exception= {}", e.toString());
        }

        return base64;
    }

    public static boolean verifyRsaSignature(byte[] data, byte[] sign) throws Exception {
        try {
            String path = SecurityUtils.class.getResource("/").getPath() + "public.pem";
            path = URLDecoder.decode(path, "utf-8");

            byte[] publicKey = readKeyByteFromFile(path);

            //转换公钥材料
            //实例化密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //初始化公钥
            //密钥材料转换
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
            //产生公钥
            PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
            //实例化Signature
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            //初始化Signature
            signature.initVerify(pubKey);
            //更新
            signature.update(data);
            //验证
            return signature.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * AES加密
     *
     * @param content 需要加密的内容
     * @return
     */
    public static String encryptWithAes(String content, String password) {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                String hex = Integer.toHexString(result[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param hexStr 待解密内容
     * @return
     */
    public static String decryptWithAes(String hexStr, String password) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] content = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            content[i] = (byte) (high * 16 + low);
        }

        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}


