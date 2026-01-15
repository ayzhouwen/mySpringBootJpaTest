package com.zw.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.zw.entity.User;
import com.zw.entity.vo.UserVo;
import com.zw.repository.UserRepository;
import com.zw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
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
        user2.setUsername("哈哈"+DateUtil.now());
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
    /**
     * 测试事务中遇到的一些问题
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testTransaction(User user) {
        User user2=new User();
        user2.setEmail("哈哈"+DateUtil.now()+"@qq.com");
        user2.setUsername("哈哈"+DateUtil.now());
        userRepository.save(user2);
        List<UserVo> userVos=userRepository.findUserVoByNativeSql(user.getUsername());
        if (CollUtil.isNotEmpty(userVos)){
                log.info("返回接口查询结果：{}", JSONUtil.toJsonStr(userVos));
        }
        List<Map<String, Object>> userVos2=userRepository.findUserVoByNativeSql2(user.getUsername());
        if (CollUtil.isNotEmpty(userVos2)){
            log.info("返回map查询结果：{}", JSONUtil.toJsonStr(userVos2));
        }
        List<User> users=userRepository.findUserVoByJpqlReturnEntity("%" + user.getUsername() + "%");
        if (CollUtil.isNotEmpty(users)){
            for (User u : users) {
                //这里会写入sql
                u.setEmail(DateUtil.now()+"@-查询实体返回测试-qq.com");
                log.info("返回jpql查询结果：{}", JSONUtil.toJsonStr(u));
            }
        }

        return "success:"+DateUtil.now();
    }
}