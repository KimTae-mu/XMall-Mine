package com.alva.manager.service;

import com.alva.common.pojo.DataTablesResult;
import com.alva.manager.pojo.TbPermission;
import com.alva.manager.pojo.TbRole;
import com.alva.manager.pojo.TbUser;

import java.util.List;
import java.util.Set;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public interface UserService {
    Set<String> getRoles(String username);

    Set<String> getPermissions(String username);

    TbUser getUserByUsername(String username);

    List<TbRole> getAllRoles();

    int addUser(TbUser tbUser);

    boolean getUserByEmail(String email);

    boolean getUserByPhone(String phone);

    boolean getUserByName(String username);

    int updateUser(TbUser tbUser);

    int addRole(TbRole role);

    TbRole getRoleByRoleName(String name);

    Long countUser();

    int delUser(Long[] ids);

    int changeUserState(Long id, int i);

    int changePassword(TbUser tbUser);

    boolean getUserByEditEmail(Long id, String email);

    boolean getUserByEditPhone(Long id, String phone);

    boolean getUserByEditName(Long id, String username);

    DataTablesResult getRoleList();

    boolean getRoleByEditName(int id, String name);

    int updateRole(TbRole tbRole);

    int deleteRole(int id);

    Long countRole();

    DataTablesResult getPermissionList();

    int addPermission(TbPermission tbPermission);

    int updatePermission(TbPermission tbPermission);

    int deletePermission(int id);

    Long countPermission();

    DataTablesResult getUserList();
}
