package com.alva.manager.mapper;

import com.alva.manager.pojo.TbExpress;
import com.alva.manager.pojo.TbExpressExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbExpressMapper {
    long countByExample(TbExpressExample example);

    int deleteByExample(TbExpressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbExpress record);

    int insertSelective(TbExpress record);

    List<TbExpress> selectByExample(TbExpressExample example);

    TbExpress selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbExpress record, @Param("example") TbExpressExample example);

    int updateByExample(@Param("record") TbExpress record, @Param("example") TbExpressExample example);

    int updateByPrimaryKeySelective(TbExpress record);

    int updateByPrimaryKey(TbExpress record);
}