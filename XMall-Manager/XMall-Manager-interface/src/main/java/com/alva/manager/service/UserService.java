package com.alva.manager.service;

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
}
