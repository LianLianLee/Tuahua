package com.tanhua.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.dubbo.mappers.UserInfoMapper;
import com.tanhua.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@DubboService
public class UserInfoApiImpl implements UserInfoApi{

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 根据用户ID查询用户信息
     */
    @Override
    public UserInfo findById(Long id) {
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(UserInfo::getId,id);
        UserInfo userInfo = userInfoMapper.selectOne(qw);
        return null;
    }

    /**
     * 更新用户信息
     */
    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    /**
     * 保存用户信息
     */
    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    /**
     * 查询所有用户详情
     * @param ids
     * @param userInfo
     * @return
     */
    @Override
    public Map<Long, UserInfo> findByIds(List<Long> ids, UserInfo userInfo) {
        QueryWrapper qw = new QueryWrapper();
        //1、用户id列表
        qw.in("id",ids);
        //2、添加筛选条件
        if(userInfo != null) {
            if(userInfo.getAge() != null) {
                qw.lt("age",userInfo.getAge());
            }
            if(!StringUtils.isEmpty(userInfo.getGender())) {
                qw.eq("gender",userInfo.getGender());
            }
            if(!StringUtils.isEmpty(userInfo.getNickname())) {
                qw.like("nickname",userInfo.getNickname());
            }
        }
        List<UserInfo> list = userInfoMapper.selectList(qw);
        Map<Long, UserInfo> map = CollUtil.fieldValueMap(list,"id");
        return map;
    }

}
