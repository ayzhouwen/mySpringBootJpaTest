package com.zw.repository;

import com.zw.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 用户数据访问层（JPA自动实现CRUD）
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // 自定义查询：根据用户名模糊查询
    List<User> findByUsernameLike(String username);

    // 自定义查询：根据年龄查询
    List<User> findByAge(Integer age);
}