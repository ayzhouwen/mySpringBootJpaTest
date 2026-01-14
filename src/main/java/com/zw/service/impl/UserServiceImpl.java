package com.zw.service.impl;

import cn.hutool.core.date.DateUtil;
import com.zw.entity.User;
import com.zw.repository.UserRepository;
import com.zw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    /**
     * 测试事务中对查询出来的实体类进行修改，也会修改到数据库中
     * @param id
     * @return
     */
    @Override
    //这个事务find查出来的实体类修改，会更新sql
//    @Transactional(rollbackFor = Exception.class)
    //readOnly = true ，查询出来的实体类的修改不会更新sql
    @Transactional(rollbackFor = Exception.class,readOnly = true)
    public Optional<User> getUserById(String id) {
        Optional<User> u= userRepository.findById(id);
        //测试这里会不会对数据修改
        u.get().setEmail(DateUtil.now()+"@qq.com");
        userRepository.save(u.get());

        User user2=new User();
        user2.setEmail("哈哈@qq.com");
        userRepository.save(user2);
        return u;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUserByUsername(String username) {
        return userRepository.findByUsernameLike("%" + username + "%");
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}