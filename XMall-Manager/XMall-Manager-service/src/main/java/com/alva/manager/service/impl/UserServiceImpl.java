package com.alva.manager.service.impl;

import com.alva.common.exception.XmallException;
import com.alva.manager.mapper.TbRoleMapper;
import com.alva.manager.mapper.TbUserMapper;
import com.alva.manager.pojo.TbRole;
import com.alva.manager.pojo.TbRoleExample;
import com.alva.manager.pojo.TbUser;
import com.alva.manager.pojo.TbUserExample;
import com.alva.manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private TbRoleMapper tbRoleMapper;

    @Override
    public Set<String> getRoles(String username) {
        return tbUserMapper.getRoles(username);
    }

    @Override
    public Set<String> getPermissions(String username) {
        return tbUserMapper.getPermissions(username);
    }

    @Override
    public TbUser getUserByUsername(String username) {
        List<TbUser> list;
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andStateEqualTo(1);
        try {
            list = tbUserMapper.selectByExample(example);
        } catch (Exception e) {
            throw new XmallException("通过用户名获取用户信息失败");
        }
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<TbRole> getAllRoles() {
        TbRoleExample tbRoleExample = new TbRoleExample();
        List<TbRole> list = tbRoleMapper.selectByExample(tbRoleExample);
        if (list == null) {
            throw new XmallException("获取所有角色列表失败");
        }
        return list;
    }
}
