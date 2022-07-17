package com.tanhua.server.service;

import com.tanhua.autoconfig.template.AipFaceTemplate;
import com.tanhua.autoconfig.template.OssTemplate;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.vo.UserInfoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserInfoService {

    @DubboReference
    private UserInfoApi userInfoApi;

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private AipFaceTemplate aipFaceTemplate;

    //保存用户信息
    public void save(UserInfo userInfo) {
        userInfoApi.save(userInfo);
    }

    /**
     * 上传用户头像
     * @param headPhoto
     * @param id
     */
    public void updateHead(MultipartFile headPhoto, Long id) throws IOException {

        //1.将图片上传oss
        String imageUrl = ossTemplate.upload(headPhoto.getOriginalFilename(), headPhoto.getInputStream());
        //2.百度判断是否是人类
        boolean isFace = aipFaceTemplate.detect(imageUrl);
        //3.不是人返回
        if(!isFace){
            throw new RuntimeException("不包含人脸");
        }else {
            //4.是就保存到本地数据库
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setAvatar(imageUrl);
            userInfoApi.update(userInfo);
        }
    }

    /**
     * 查询用户全部信息 --根据id查询
     * @param userID
     * @return
     */
    public UserInfoVo findById(Long userID) {
        UserInfo userInfo = userInfoApi.findById(userID);

        UserInfoVo userInfoVo = new UserInfoVo();

        BeanUtils.copyProperties(userInfo,userInfoVo);

        if(userInfo.getAge() != null) {
            userInfoVo.setAge(userInfo.getAge().toString());
        }

        return userInfoVo;
    }

    /**
     * 更新用户信息
     * @param userInfo
     */
    public void update(UserInfo userInfo) {
        userInfoApi.update(userInfo);
    }

}
