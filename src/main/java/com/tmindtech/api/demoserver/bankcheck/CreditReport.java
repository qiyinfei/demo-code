package com.tmindtech.api.demoserver.bankcheck;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis_test")
public class CreditReport {
    private static final String KEY = "redis_key";

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/add")
    public Object addRedisValue() {
        redisTemplate.opsForValue().set(KEY, "你好，我是redis!我的有效期是40秒...", 40, TimeUnit.SECONDS);
        return "我的值为：" + redisTemplate.opsForValue().get(KEY) + "，剩余时间为：" + redisTemplate.getExpire(KEY);
    }

    @GetMapping("/put/{value}")
    public Object putRedisValue(@PathVariable("value") String value) {
        redisTemplate.opsForValue().set(KEY, "我正在修改redis的值，值为：" + value, 90, TimeUnit.SECONDS);
        return "我的值为：" + redisTemplate.opsForValue().get(KEY) + "，剩余时间为：" + redisTemplate.getExpire(KEY);
    }

    @GetMapping("/get")
    public Object getRedisValue() {
        return "我的值为：" + redisTemplate.opsForValue().get(KEY) + "，剩余时间为：" + redisTemplate.getExpire(KEY);
    }

}
