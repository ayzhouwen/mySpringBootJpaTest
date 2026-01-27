package com.zw.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.zw.entity.Depart;
import com.zw.entity.User;
import com.zw.entity.vo.UserVo;
import com.zw.repository.UserRepository;
import com.zw.service.UserService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

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
/**
     * 测试模糊查询,注意jpa默认会过滤空值，也就是 .setCode(null) 不会生成 code is null
     * 懒加载后使用相关对象必须在事务中才不报错
     * @param username
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> getUserByUsername(String username) {

        List<User> userList = userRepository.findAll(Example.of(
                new User().setAge(18).setUsername("aaa").setDepart(new Depart().setType("1").setCode(null))));
//        List<User> userList = userRepository.findAll(Example.of(
//                new User().setAge(18).setUsername("aaa")));

        User user = userRepository.findById("2014654625591988224").orElse(null);
        log.info(user.getDepart().getName());
        List<User> userLikeList = userRepository.findByUsernameLike("%" + username + "%");
        return userLikeList;
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    /**
     * 测试事务中遇到的一些问题
     * 注意：entityManager执行所有方法，都会先执行
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testTransaction(User user) {
        User user2=new User();
        user2.setEmail("哈哈"+DateUtil.now()+"@qq.com");
        user2.setUsername("哈哈"+DateUtil.now());
        //测试删除
        entityManager.flush();
        entityManager.clear();
        //注意前后两次hashcode一样，close的代理方法内部其实什么也不做
        log.info("EM close前:{}",entityManager.unwrap(Session.class).hashCode());
        entityManager.close();//即使执行close，内部执行到SharedEntityManagerInvocationHandler的invoke方法
        log.info("EM close后:{}",entityManager.unwrap(Session.class).hashCode());
        userRepository.save(user2);
        log.info("EM close后并且save后:{}",entityManager.unwrap(Session.class).hashCode());


       // userRepository.deleteById(user2.getId());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String testJdbcTemplate(User user) {
        String sql="select * from t_user where username like '%"+user.getUsername()+"%'";
        List<User> users=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(User.class));
        if (CollUtil.isNotEmpty(users)){
            for (User u : users) {
                //这里不会写入sql
                u.setEmail(DateUtil.now()+"@-查询修改JDBC返回测试-qq.com");
                log.info("返回jdbcTemplate查询结果：{}", JSONUtil.toJsonStr(u));
            }
        }
        return "";
    }
}