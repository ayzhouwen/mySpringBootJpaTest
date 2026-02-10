package com.zw.repository;

import com.zw.entity.TOrder;
import com.zw.entity.dto.TOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问层（JPA自动实现CRUD）
 */
@Repository
public interface OrderRepository extends JpaRepository<TOrder, String>, JpaSpecificationExecutor<TOrder> {

    // 方法1：按状态分页查询（自动分页）
    Page<TOrder> findByStatus(Integer status, Pageable pageable);

    // 方法2：自定义 JPQL 查询 + 分页
    @Query("SELECT new com.zw.entity.dto.TOrderDto(o.id, o.orderNo, o.userId, o.status, o.totalAmount) " +
            "FROM TOrder o " +
            "WHERE o.status = :status")
    Page<TOrderDto> findDtoByStatus(@Param("status") Integer status, Pageable pageable);
}