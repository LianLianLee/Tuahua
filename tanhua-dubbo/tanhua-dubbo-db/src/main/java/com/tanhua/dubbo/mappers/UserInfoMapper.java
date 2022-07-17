package com.tanhua.dubbo.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.model.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("Select * from tb_user_info where id in (\n"+
    "Select black_user_id from tb_black_list where user_id=#{userId}\n"+
    ")")
    IPage<UserInfo> findBlackList(Page pages, Long userId);
}
