package com.xkb.couple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xkb.couple.pojo.entity.Couple;
import org.apache.ibatis.annotations.Mapper;

/**
 * 情侣表映射器
 * @author xuwatermelon
 * @date 2026/02/18
 */
@Mapper
public interface CoupleMapper extends BaseMapper<Couple> {
}
