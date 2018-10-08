package com.alva.manager.service.impl;

import com.alva.common.exception.XmallException;
import com.alva.common.pojo.DataTablesResult;
import com.alva.manager.dto.RoleDto;
import com.alva.manager.mapper.TbPermissionMapper;
import com.alva.manager.mapper.TbRoleMapper;
import com.alva.manager.mapper.TbRolePermMapper;
import com.alva.manager.mapper.TbUserMapper;
import com.alva.manager.pojo.*;
import com.alva.manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

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

    @Autowired
    private TbRolePermMapper tbRolePermMapper;

    @Autowired
    private TbPermissionMapper tbPermissionMapper;

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

    @Override
    public int addUser(TbUser tbUser) {
        if (!getUserByName(tbUser.getUsername())) {
            throw new XmallException("用户名已存在");
        }
        if (!getUserByPhone(tbUser.getPhone())) {
            throw new XmallException("手机号已存在");
        }
        if (!getUserByEmail(tbUser.getEmail())) {
            throw new XmallException("邮箱已存在");
        }
        String md5Pass = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(md5Pass);
        tbUser.setState(1);
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());

        if (tbUserMapper.insert(tbUser) != 1) {
            throw new XmallException("添加用户失败");
        }
        return 1;

    }

    @Override
    public boolean getUserByEmail(String email) {
        List<TbUser> list;
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(email);
        try {
            list = tbUserMapper.selectByExample(example);
        } catch (Exception e) {
            throw new XmallException("根据Email查询用户失败");
        }
        if (list.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean getUserByPhone(String phone) {
        List<TbUser> list;
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneEqualTo(phone);
        try {
            list = tbUserMapper.selectByExample(example);
        } catch (Exception e) {
            throw new XmallException("根据Phone查询用户失败");
        }
        if (list.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean getUserByName(String username) {
        List<TbUser> list;
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        try {
            list = tbUserMapper.selectByExample(example);
        } catch (Exception e) {
            throw new XmallException("根据Username查询用户失败");
        }
        if (list.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public int updateUser(TbUser tbUser) {
        TbUser old = tbUserMapper.selectByPrimaryKey(tbUser.getId());

        tbUser.setPassword(old.getPassword());
        tbUser.setState(old.getState());
        tbUser.setCreated(old.getCreated());
        tbUser.setUpdated(new Date());

        if (tbUserMapper.updateByPrimaryKey(tbUser) != 1) {
            throw new XmallException("更新用户信息失败");
        }
        return 1;
    }

    @Override
    public int addRole(TbRole role) {
        if (getRoleByRoleName(role.getName()) != null) {
            throw new XmallException("该角色名已存在");
        }
        if (tbRoleMapper.insert(role) != 1) {
            throw new XmallException("添加角色失败");
        }
        if (role.getRoles() != null) {
            TbRole newRole = getRoleByRoleName(role.getName());
            for (int i = 0; i < role.getRoles().length; i++) {
                TbRolePerm tbRolePerm = new TbRolePerm();
                tbRolePerm.setRoleId(newRole.getId());
                tbRolePerm.setPermissionId(role.getRoles()[i]);

                if (tbRolePermMapper.insert(tbRolePerm) != 1) {
                    throw new XmallException("添加角色-权限失败");
                }
            }
        }
        return 1;
    }

    @Override
    public TbRole getRoleByRoleName(String name) {

        TbRoleExample example = new TbRoleExample();
        TbRoleExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<TbRole> list = new ArrayList<>();
        try {
            list = tbRoleMapper.selectByExample(example);
        } catch (Exception e) {
            throw new XmallException("通过角色名获取角色失败");
        }
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Long countUser() {
        TbUserExample example = new TbUserExample();
        Long result = tbUserMapper.countByExample(example);
        if (result == null) {
            throw new XmallException("统计用户数失败!");
        }
        return result;
    }

    @Override
    public int delUser(Long[] ids) {
        if (ids == null || ids.length < 0) {
            throw new XmallException("参数有误!");
        }
        int result = tbUserMapper.deleteByIds(ids);
        if (result != 1) {
            throw new XmallException("删除有误!");
        }
        return 1;
    }

    @Override
    public int changeUserState(Long id, int state) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(id);
        tbUser.setState(state);
        if (tbUserMapper.updateByPrimaryKey(tbUser) != 1) {
            throw new XmallException("更新用户状态失败");
        }
        return 1;
    }

    @Override
    public int changePassword(TbUser tbUser) {
        TbUser old = tbUserMapper.selectByPrimaryKey(tbUser.getId());
        old.setUpdated(new Date());
        String md5Pass = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        old.setPassword(md5Pass);
        if (tbUserMapper.updateByPrimaryKey(old) != 1) {
            throw new XmallException("修改用户密码失败");
        }
        return 1;
    }

    @Override
    public boolean getUserByEditEmail(Long id, String email) {
        TbUser tbUser = getUserById(id);
        boolean result = true;
        if (tbUser.getEmail() == null || !tbUser.getEmail().equals(email)) {
            result = getUserByEmail(email);
        }
        return result;
    }

    @Override
    public boolean getUserByEditPhone(Long id, String phone) {
        TbUser userById = getUserById(id);
        boolean result = true;
        if (userById.getPhone() == null || !userById.getPhone().equals(phone)) {
            result = getUserByPhone(phone);
        }
        return result;
    }

    @Override
    public boolean getUserByEditName(Long id, String username) {
        TbUser userById = getUserById(id);
        boolean result = true;
        if (userById.getUsername() == null || !userById.getUsername().equals(username)) {
            result = getUserByName(username);
        }
        return result;
    }

    @Override
    public DataTablesResult getRoleList() {
        DataTablesResult result = new DataTablesResult();
        List<RoleDto> list = new ArrayList<>();
        TbRoleExample example = new TbRoleExample();
        List<TbRole> tbRoleList = tbRoleMapper.selectByExample(example);

        if (tbRoleList == null) {
            throw new XmallException("获取角色列表失败");
        }
        for (TbRole tbRole : tbRoleList) {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(tbRole.getId());
            roleDto.setName(tbRole.getName());
            roleDto.setDescription(tbRole.getDescription());

            List<String> permissions = tbUserMapper.getPermsByRoleId(tbRole.getId());
            String names = "";
            if (permissions.size() > 1) {
                names += permissions.get(0);
                for (int i = 1; i < permissions.size(); i++) {
                    names += "|" + permissions.get(i);
                }
            } else if (permissions.size() == 1) {
                names += permissions.get(0);
            }
            roleDto.setPermissions(names);

            list.add(roleDto);
        }
        result.setData(list);
        return result;
    }

    @Override
    public boolean getRoleByEditName(int id, String name) {
        TbRole tbRole = tbRoleMapper.selectByPrimaryKey(id);
        TbRole newRole = null;
        if (tbRole == null) {
            throw new XmallException("通过ID获取角色失败");
        }
        if (!tbRole.getName().equals(name)) {
            newRole = getRoleByRoleName(name);
        }
        if (newRole == null) {
            return true;
        }
        return false;
    }

    @Override
    public int updateRole(TbRole tbRole) {
        if (!getRoleByEditName(tbRole.getId(), tbRole.getName())) {
            throw new XmallException("该角色名已存在");
        }
        if (tbRoleMapper.updateByPrimaryKey(tbRole) != 1) {
            throw new XmallException("更新角色失败");
        }
        if (tbRole.getRoles() != null) {
            //删除已有角色-权限
            TbRolePermExample example = new TbRolePermExample();
            TbRolePermExample.Criteria criteria = example.createCriteria();
            criteria.andRoleIdEqualTo(tbRole.getId());
            List<TbRolePerm> tbRolePerms = tbRolePermMapper.selectByExample(example);
            if (tbRolePerms != null) {
                for (TbRolePerm tbRolePerm : tbRolePerms) {
                    if (tbRolePermMapper.deleteByPrimaryKey(tbRolePerm.getId()) != 1) {
                        throw new XmallException("删除角色权限失败");
                    }
                }
            }
            //新增
            for (int i = 0; i < tbRole.getRoles().length; i++) {
                TbRolePerm tbRolePerm = new TbRolePerm();
                tbRolePerm.setRoleId(tbRole.getId());
                tbRolePerm.setPermissionId(tbRole.getRoles()[i]);

                if (tbRolePermMapper.insert(tbRolePerm) != 1) {
                    throw new XmallException("编辑角色-权限失败");
                }
            }
        } else {
            TbRolePermExample example = new TbRolePermExample();
            TbRolePermExample.Criteria criteria = example.createCriteria();
            criteria.andRoleIdEqualTo(tbRole.getId());
            List<TbRolePerm> tbRolePerms = tbRolePermMapper.selectByExample(example);
            if (tbRolePerms != null) {
                for (TbRolePerm tbRolePerm : tbRolePerms) {
                    if (tbRolePermMapper.deleteByPrimaryKey(tbRolePerm.getId()) != 1) {
                        throw new XmallException("删除角色权限失败");
                    }
                }
            }
        }
        return 1;
    }

    @Override
    public int deleteRole(int id) {
        List<String> list = tbRoleMapper.getUsedRoles(id);
        if (list == null) {
            throw new XmallException("查询用户角色失败");
        }
        if (list.size() > 0) {
            return 0;
        }
        if (tbRoleMapper.deleteByPrimaryKey(id) != 1) {
            throw new XmallException("删除角色失败");
        }
        TbRolePermExample example = new TbRolePermExample();
        TbRolePermExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(id);
        List<TbRolePerm> tbRolePerms = tbRolePermMapper.selectByExample(example);
        if (tbRolePerms == null) {
            throw new XmallException("查询角色权限失败");
        }
        for (TbRolePerm tbRolePerm : tbRolePerms) {
            if (tbRolePermMapper.deleteByPrimaryKey(tbRolePerm.getId()) != 1) {
                throw new XmallException("删除角色权限失败");
            }
        }
        return 1;
    }

    @Override
    public Long countRole() {
        TbRoleExample tbRoleExample = new TbRoleExample();
        long count = tbRoleMapper.countByExample(tbRoleExample);
        return count;
    }

    @Override
    public DataTablesResult getPermissionList() {
        DataTablesResult result = new DataTablesResult();
        TbPermissionExample example = new TbPermissionExample();
        List<TbPermission> tbPermissions = tbPermissionMapper.selectByExample(example);
        if (tbPermissions == null) {
            throw new XmallException("获取权限列表失败");
        }
        result.setSuccess(true);
        result.setData(tbPermissions);
        return result;
    }

    @Override
    public int addPermission(TbPermission tbPermission) {
        if (tbPermissionMapper.insert(tbPermission) != 1) {
            throw new XmallException("添加权限失败");
        }
        return 1;
    }

    @Override
    public int updatePermission(TbPermission tbPermission) {
        if (tbPermissionMapper.updateByPrimaryKey(tbPermission) != 1) {
            throw new XmallException("更新权限失败");
        }
        return 1;
    }

    @Override
    public int deletePermission(int id) {
        if (tbPermissionMapper.deleteByPrimaryKey(id) != 1) {
            throw new XmallException("删除权限失败");
        }
        TbRolePermExample example = new TbRolePermExample();
        TbRolePermExample.Criteria criteria = example.createCriteria();
        criteria.andPermissionIdEqualTo(id);
        tbRolePermMapper.deleteByExample(example);
        return 1;
    }

    @Override
    public Long countPermission() {
        TbPermissionExample example = new TbPermissionExample();
        Long result = tbPermissionMapper.countByExample(example);
        if (result == null) {
            throw new XmallException("统计权限数目失败");
        }
        return result;
    }

    @Override
    public DataTablesResult getUserList() {
        DataTablesResult result = new DataTablesResult();
        TbUserExample example = new TbUserExample();

        List<TbUser> users = tbUserMapper.selectByExample(example);
        if (users == null) {
            throw new XmallException("获取用户列表失败");
        }
        for (TbUser tbUser : users) {
            String names = "";
            Iterator<String> iterator = getRoles(tbUser.getUsername()).iterator();
            while (iterator.hasNext()) {
                names += iterator.next() + " ";
            }
            tbUser.setPassword("");
            tbUser.setRoleNames(names);
        }
        result.setData(users);
        return result;
    }

    private TbUser getUserById(Long id) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(id);
        if (tbUser == null) {
            throw new XmallException("通过ID获取用户失败");
        }
        tbUser.setPassword("");
        return tbUser;
    }

}
