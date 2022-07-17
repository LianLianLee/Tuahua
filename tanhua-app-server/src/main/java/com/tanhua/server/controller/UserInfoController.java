package com.tanhua.server.controller;

import com.tanhua.commons.utils.JwtUtils;
import com.tanhua.model.vo.UserInfoVo;
import com.tanhua.server.service.UserInfoService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 查询用户资料
     */
    public ResponseEntity userInfo(Long userID, @RequestHeader("Authorization") String token)
    {
        //1.校验token
        boolean verifyToken = JwtUtils.verifyToken(token);
        if(!verifyToken) {
            return ResponseEntity.status(401).body(null);
        }
        //2.获取用户信息
        Claims claims = JwtUtils.getClaims(token);
        String id = claims.get("id").toString();

        if (userID == null) {
            userID = Long.valueOf(id);
        }

        UserInfoVo userInfo = userInfoService.findById(userID);

        return ResponseEntity.ok(userInfo);
    }
}
