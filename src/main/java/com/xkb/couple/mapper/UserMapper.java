package com.xkb.couple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xkb.couple.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 数据库访问层
 * @author xuwatermelon
 * @date 2026/02/06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
