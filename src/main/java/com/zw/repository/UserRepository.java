package com.zw.repository;

import com.zw.entity.User;
import com.zw.entity.vo.UserVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

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
     * 方案1：原生SQL查询映射到VO,UserVo必须是接口，并且结果不能被json序列化
     * 注意：参数绑定 ?1 对应方法参数 keyword
     */
    @Query(value = "SELECT " +
            "u.username AS username, " +
            "u.age  AS age, " +
            "u.email  AS email, " +
            "u.create_user_id  AS createUserId, " +     // 数据库字段 -> Java属性
            "u.create_time AS  createTime, " +         // 注意别名必须匹配
            "u.update_user_id  AS updateUserId, " +
            "u.update_time  AS updateTime " +
            "FROM t_user u " +                        // 这里是数据库表名
            "WHERE u.username LIKE CONCAT('%', ?1, '%')",
            nativeQuery = true)
    List<UserVo> findUserVoByNativeSql(String keyword);

    /**
     * 方案1.1：原生SQL查询映射到Map，可以被json序列化
     */
    @Query(value = "SELECT " +
            "u.username AS username, " +
            "u.age  AS age, " +
            "u.email  AS email, " +
            "u.create_user_id  AS createUserId, " +     // 数据库字段 -> Java属性
            "u.create_time AS  createTime, " +         // 注意别名必须匹配
            "u.update_user_id  AS updateUserId, " +
            "u.update_time  AS updateTime " +
            "FROM t_user u " +                        // 这里是数据库表名
            "WHERE u.username LIKE CONCAT('%', ?1, '%')",
            nativeQuery = true)
    List<Map<String, Object>> findUserVoByNativeSql2(String keyword);
    /**
     * 方案2.1：JPQL 查询返回 User，注意这必须要查询出所有的列
     * 注意：要注意后面的User属性变动可能会写入到数据库
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT('%', :keyword, '%')")
    List<User> findUserVoByJpqlReturnEntity(String keyword);

    /**
     * 方案2.2：JPQL 查询部分列不能用实体类，所有列才行
     */

    @Query("SELECT  " +
            "u.username, u.age, u.email, " +
            "u.createUserId, u.createTime, " +
            "u.updateUserId, u.updateTime " +
            "FROM User u " +
            "WHERE u.username LIKE CONCAT('%', :keyword, '%')")
    List<UserVo> findUserVoByJpqlReturnEntity2(String keyword);

}