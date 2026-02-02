package com.zw.repository;

import com.zw.entity.TOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问层（JPA自动实现CRUD）
 */
@Repository
public interface OrderItemRepository extends JpaRepository<TOrderItem, String> {
}