package com.tanhua.server.controller;

import com.tanhua.commons.utils.JwtUtils;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.server.service.UserInfoService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 保存用户信息
     *   UserInfo
     *   请求头中携带token
     */
    @PostMapping("/loginReginfo")
    public ResponseEntity loginReginfo(@RequestBody UserInfo userInfo,
                                       @RequestHeader("Authorization") String token) {
        //1、判断token是否合法
        boolean verifyToken = JwtUtils.verifyToken(token);
        if(!verifyToken) {
            return ResponseEntity.status(401).body(null);
        }
        //2、向userinfo中设置用户id
        Claims claims = JwtUtils.getClaims(token);
        Integer id = (Integer) claims.get("id");
        userInfo.setId(Long.valueOf(id));
        //3、调用service
        userInfoService.save(userInfo);
        return ResponseEntity.ok(null);
    }

    /**
     *上传用户头像
     */
    @PostMapping("/loginReginfo/head")
    public ResponseEntity head(MultipartFile headPhoto, @RequestHeader("Authorization") String token) throws IOException {
        //1、判断token是否合法
        boolean verifyToken = JwtUtils.verifyToken(token);
        if(!verifyToken) {
            return ResponseEntity.status(401).body(null);
        }
        //2、向userinfo中设置用户id
        Claims claims = JwtUtils.getClaims(token);
        Long id = (Long) claims.get("id");
        //3、调用service
        userInfoService.updateHead(headPhoto,id);
        return ResponseEntity.ok("上传成功!");
    }

    /**
     * 查询用户资料
     */

    /**
     * 更新用户资料
     */
    @PutMapping
    public ResponseEntity updateUserInfo(@RequestBody UserInfo userInfo,@RequestHeader("Authorization") String token) {
        //1、判断token是否合法
        boolean verifyToken = JwtUtils.verifyToken(token);
        if(!verifyToken) {
            return ResponseEntity.status(401).body(null);
        }
        //2、获取id
        Claims claims = JwtUtils.getClaims(token);
        Integer id = (Integer) claims.get("id");

        //设置id
        userInfo.setId(Long.valueOf(id));
        userInfoService.update(userInfo);
        return ResponseEntity.ok(null);
    }


}
