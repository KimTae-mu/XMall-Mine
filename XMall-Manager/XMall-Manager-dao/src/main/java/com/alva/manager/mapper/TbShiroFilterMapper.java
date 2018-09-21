package com.alva.manager.mapper;

import com.alva.manager.pojo.TbShiroFilter;
import com.alva.manager.pojo.TbShiroFilterExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbShiroFilterMapper {
    long countByExample(TbShiroFilterExample example);

    int deleteByExample(TbShiroFilterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbShiroFilter record);

    int insertSelective(TbShiroFilter record);

    List<TbShiroFilter> selectByExample(TbShiroFilterExample example);

    TbShiroFilter selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbShiroFilter record, @Param("example") TbShiroFilterExample example);

    int updateByExample(@Param("record") TbShiroFilter record, @Param("example") TbShiroFilterExample example);

    int updateByPrimaryKeySelective(TbShiroFilter record);

    int updateByPrimaryKey(TbShiroFilter record);
}