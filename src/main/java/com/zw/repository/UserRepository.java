package com.zw.repository;

import com.zw.entity.User;
import com.zw.entity.vo.UserVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    /**
     * 方案1：原生SQL查询映射到VO
     * 注意：参数绑定 ?1 对应方法参数 keyword
     */
    @Query(value = "SELECT " +
            "u.username  username, " +
            "u.age  age, " +
            "u.email  email, " +
            "u.create_user_id  createUserId, " +     // 数据库字段 -> Java属性
            "u.create_time  createTime, " +         // 注意别名必须匹配
            "u.update_user_id  updateUserId, " +
            "u.update_time  updateTime " +
            "FROM t_user u " +                        // 这里是数据库表名
            "WHERE u.username LIKE %?1%",
            nativeQuery = true)
    List<UserVo> findUserVoByNativeSql(String keyword);


    /**
     * 方案2：JPQL 查询返回 Tuple
     * 注意：这里查询的是 User 实体的属性
     */
    @Query("SELECT " +
            "u.username, u.age, u.email, " +
            "u.createUserId, u.createTime, " +
            "u.updateUserId, u.updateTime " +
            "FROM User u " +
            "WHERE u.username LIKE %:keyword%")
    List<UserVo> findUserVoByJpql(String keyword);
}