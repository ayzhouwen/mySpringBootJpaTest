package com.zw.service.impl;

import com.zw.entity.User;
import com.zw.repository.UserRepository;
import com.zw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
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