package com.zw.repository;

import com.zw.entity.TOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问层（JPA自动实现CRUD）
 */
@Repository
public interface OrderRepository extends JpaRepository<TOrder, String> {
}