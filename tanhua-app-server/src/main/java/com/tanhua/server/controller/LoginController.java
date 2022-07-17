package com.tanhua.server.controller;

import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author lym
 */
@RequestMapping("user")
@RestController
public class LoginController {


    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody Map<String,String> phonemap)
    {
        String phone = phonemap.get("phone");
        userService.sendMsg(phone);
        return ResponseEntity.ok(null);
    }

    /**
     * 校验登录
     */
    @PostMapping("/loginVerification")
    public ResponseEntity loginVerification(@RequestBody Map map) {
        //1 调用map集合获取请求参数
        String phone = (String) map.get("phone");
        String code = (String) map.get("verificationCode");

        //jwts
        Map retMap = userService.verify(phone,code);

        return ResponseEntity.ok(retMap);
    }
}
