package com.tmindtech.api.demoserver.example;

import com.alibaba.fastjson.JSONObject;
import com.github.cage.Cage;
import com.github.cage.token.RandomTokenGenerator;
import com.google.common.base.Strings;
import com.tmindtech.api.demoserver.base.annotation.AwesomeParam;
import com.tmindtech.api.demoserver.example.model.AuthInfoReq;
import com.tmindtech.api.demoserver.example.model.CarrierConfigReq;
import com.tmindtech.api.demoserver.example.model.ImageData;
import com.tmindtech.api.demoserver.example.model.LabelData;
import com.tmindtech.api.demoserver.example.model.LabelInfo;
import com.tmindtech.api.demoserver.example.model.PhoneData;
import com.tmindtech.api.demoserver.example.model.PictureData;
import com.tmindtech.api.demoserver.example.model.TokenReq;
import com.tmindtech.api.demoserver.example.model.TokenRes;
import com.tmindtech.api.demoserver.example.model.User;
import com.tmindtech.api.demoserver.example.service.Customer;
import com.tmindtech.api.demoserver.example.service.ExampleService;
import com.tmindtech.api.demoserver.example.service.PrinterService;
import com.tmindtech.api.demoserver.example.service.Producer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/examples")
public class ExampleController {

    @Value("${sesame_credit_app_id}")
    String appId;

    @Value("${sesame_credit_app_key}")
    String appKey;

    @Value("${carrier_info_token_url}")
    String tokenUrl;

    @Value("${carrier_info_init_config_url}")
    String initConfigUrl;

    @Value("${carrier_info_sms_url}")
    String smsUrl;

    @Value("${carrier_info_picture_url}")
    String pictureUrl;

    @Value("${carrier_info_carrier_status_url}")
    String carrierStatusUrl;

    @Value("${carrier_info_second_verify_url}")
    String secondVerifyUrl;

    @Value("${carrier_info_carrier_report_url}")
    String carrierReportUrl;

    @Value("${carrier_info_login_url}")
    String loginUrl;

    @Autowired
    ExampleService exampleService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    Producer producer;

    @Autowired
    Customer customer;

    @Autowired
    PrinterService printerService;

    private RestTemplate restTemplate;

    private static final int RESULT_CODE = 1;

    private static final String TOKEN_REDIS_KEY = "carrier:token:redis:key";

    private static final String FIELD_EXTRA_TYPE = "fieldExtraType";

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @GetMapping("/picture")
    public ResponseEntity<byte[]> getPicture(HttpServletRequest request) {
        RandomTokenGenerator generator = new RandomTokenGenerator(null, 6, 0);
        String token = generator.next();
        Cage cage = new Cage();
        byte[] bytes = cage.draw(token);
        HttpSession httpSession = request.getSession();
        String sessionId = httpSession.getId();
        redisTemplate.opsForValue().set(String.format("session:%s", sessionId), token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "picture");
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
    }

    @GetMapping("/jms")
    public void jms(@RequestParam String message) {
        producer.sendMessage(message);
    }

    @PostMapping(value = "/add", consumes = "application/json; charset=utf-8")
    public Object addUser(@RequestBody User user) {
        return exampleService.addUser(user);
    }

    @GetMapping("/get")
    public Object getUserByName(@RequestParam(required = false) String name) {
        return exampleService.getUserByName(name);
    }

    @GetMapping("/get/{id}")
    public Object getUserDetail(@PathVariable long id) {
        return exampleService.getUserDetail(id);
    }

    @PutMapping("/put/{id}")
    public void updateUser(@RequestBody User user, @PathVariable long id) {
        exampleService.updateUser(user, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable long id) {
        exampleService.deleteUser(id);
    }

    @PostMapping("/print")
    public Object printPicture(@RequestParam(value = "file_import") MultipartFile file,
                               @AwesomeParam int count) {
        return printerService.printOrderTask(file, count);
    }


    @GetMapping("/token")
    public PhoneData genTokenAndInitConfig(@AwesomeParam String name,
                                           @AwesomeParam String phone,
                                           @AwesomeParam String idCard) {
        PhoneData data = new PhoneData();
        long timestamp = Timestamp.from(Instant.now()).getTime();
        String sign = getMd5Signature(timestamp);
        TokenReq tokenReq = new TokenReq(appId, timestamp, sign, phone, name, idCard);
        TokenRes tokenRes;
        try {
            tokenRes = restTemplate.postForObject(tokenUrl, tokenReq, TokenRes.class);
            if (tokenRes.retCode != RESULT_CODE) {
                throw new RuntimeException("dddd");
            }
            redisTemplate.opsForValue().set(TOKEN_REDIS_KEY, tokenRes.data.token, 30, TimeUnit.MINUTES);

            CarrierConfigReq req = new CarrierConfigReq(appId, timestamp, sign, phone, tokenRes.data.token);
            String result = restTemplate.postForObject(initConfigUrl, req, String.class);
            if (Strings.isNullOrEmpty(result)) {
                throw new RuntimeException("dddd");
            }

            int length = FIELD_EXTRA_TYPE.length();
            int indexFirst = result.indexOf(FIELD_EXTRA_TYPE) + length;
            int indexLast = result.lastIndexOf(FIELD_EXTRA_TYPE) + length;

            if (indexFirst != indexLast) {
                List<String> list = new ArrayList<>(2);
                list.add("PIC");
                list.add("SMS");
                data.fieldExtraTypes = list;
                getSmsVerify(tokenRes.data.token, timestamp, sign);
                data.base64Image = getPictureVerify(tokenRes.data.token, timestamp, sign);
            } else {
                String reg = result.substring(indexFirst, indexFirst + 15);
                if (reg.indexOf("SMS") > 0) {
                    List<String> list = new ArrayList<>(1);
                    list.add("SMS");
                    data.fieldExtraTypes = list;
                    getSmsVerify(tokenRes.data.token, timestamp, sign);
                } else if (reg.indexOf("PIC") > 0) {
                    List<String> list = new ArrayList<>(1);
                    list.add("PIC");
                    data.fieldExtraTypes = list;
                    data.base64Image = getPictureVerify(tokenRes.data.token, timestamp, sign);
                } else {
                    return null;
                }
            }
        } catch (RuntimeException re) {
            re.printStackTrace();
            throw new RuntimeException("dddd");
        }
        return data;
    }

    @PutMapping("/request")
    public ImageData getImageData(@RequestBody String str) {
        System.out.println(str);
        ImageData imageData = new ImageData("测试image msg", "测试image errorMsg", "http://localhost:8888/300dpi.png", 200);
        System.out.println("来来来咯：：：" + JSONObject.toJSONString(imageData));
        return imageData;
    }

    @GetMapping("/request")
    public LabelData getData(@RequestParam(value = "string") String string) {
        System.out.println(string);
        LabelData data = new LabelData();
        LabelInfo info = new LabelInfo("saleOrder", 111, "uuidCode");
        data.data = info;
        return data;
    }

    private void getSmsVerify(String token, Long timestamp, String sign) {
        AuthInfoReq req = new AuthInfoReq(appId, timestamp, sign, token);
        restTemplate.postForObject(smsUrl, req, String.class);
    }

    private String getPictureVerify(String token, Long timestamp, String sign) {
        AuthInfoReq req = new AuthInfoReq(appId, timestamp, sign, token);
        PictureData data = restTemplate.postForObject(pictureUrl, req, PictureData.class);
        return data.data.extra.remark;
    }

    private String getMd5Signature(Long timestamp) {
        String message = appId + timestamp + appKey;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(message.getBytes(Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }
}
