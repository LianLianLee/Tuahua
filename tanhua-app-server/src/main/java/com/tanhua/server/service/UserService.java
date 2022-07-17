package com.tanhua.server.service;

import com.tanhua.autoconfig.template.SmsTemplate;
import com.tanhua.commons.utils.JwtUtils;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.model.domain.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {


    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @DubboReference
    private UserApi userApi;

    /**
     * 发送验证码
     * @param phone
     */
    public void sendMsg(String phone) {

        //1.生成随机验证码
        String code = RandomStringUtils.randomAlphanumeric(6);
        //2.发送验证码
        smsTemplate.sendSms(phone,code);

        redisTemplate.opsForValue().set("PhoneNum_"+phone,code, Duration.ofMinutes(5));
    }

    public Map verify(String phone, String code) {

        //1. 从Redis中获取验证码
        String redisCode = redisTemplate.opsForValue().get("PhoneNum_" + phone);

        //2. 校验验证码
        if(StringUtils.isEmpty(code) || !code.equals(redisCode)) {
            throw new RuntimeException("验证码校验失败");
        }
        redisTemplate.delete("PhoneNum_"+phone);

        //3.验证勇是否注册
        boolean isNew = false;
        User user = userApi.findByMobile(phone);
        if (user == null) {
            user = new User();
            user.setMobile(phone);
            user.setPassword(DigestUtils.md5Hex("123456"));
            Long id = userApi.save(user);
            user.setId(id);
            isNew = true;
        }

        //4.生成token
        Map<String,Object> token = new HashMap<>();
        token.put("id",user.getId());
        token.put("phone",phone);
        String jwtstoken = JwtUtils.getToken(token);

        //4.封装返回
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("token",jwtstoken);
        resultMap.put("isNew",isNew);


        return resultMap;
    }
}
