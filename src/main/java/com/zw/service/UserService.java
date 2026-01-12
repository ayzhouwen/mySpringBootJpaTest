package com.zw.service;

import com.zw.entity.User;

import java.util.List;
import java.util.Optional;
public interface UserService {
    // 保存用户
    User saveUser(User user);

    // 根据ID查询用户
    Optional<User> getUserById(String id);

    // 查询所有用户
    List<User> getAllUsers();

    // 根据用户名模糊查询
    List<User> getUserByUsername(String username);

    // 删除用户
    void deleteUser(String id);
}