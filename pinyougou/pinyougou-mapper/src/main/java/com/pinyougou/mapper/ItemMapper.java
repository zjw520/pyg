package com.pinyougou.mapper;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.Item;

/**
 * ItemMapper 数据访问接口
 * @date 2019-02-27 16:21:09
 * @version 1.0
 */
public interface ItemMapper extends Mapper<Item>{

    @Select("select spec from tb_item where id = #{itemId}")
    String findSpcByItemId(Long itemId);
}