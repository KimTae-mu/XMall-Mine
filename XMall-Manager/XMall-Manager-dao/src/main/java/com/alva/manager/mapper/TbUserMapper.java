package com.alva.manager.mapper;

import com.alva.manager.pojo.TbUser;
import com.alva.manager.pojo.TbUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface TbUserMapper {
    long countByExample(TbUserExample example);

    int deleteByExample(TbUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUser record);

    int insertSelective(TbUser record);

    List<TbUser> selectByExample(TbUserExample example);

    TbUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUser record, @Param("example") TbUserExample example);

    int updateByExample(@Param("record") TbUser record, @Param("example") TbUserExample example);

    int updateByPrimaryKeySelective(TbUser record);

    int updateByPrimaryKey(TbUser record);

    Set<String> getRoles(@Param("username") String username);

    Set<String> getPermissions(@Param("username") String username);

    int deleteByIds(@Param("ids") Long[] ids);

    List<String> getPermsByRoleId(@Param("id") int id);
}