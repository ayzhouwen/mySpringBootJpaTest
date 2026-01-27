package com.zw.repository;

import com.zw.entity.Depart;
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
public interface DepartRepository extends JpaRepository<Depart, String> {
}