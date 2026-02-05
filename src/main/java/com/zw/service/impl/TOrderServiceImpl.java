package com.zw.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zw.entity.BaseEntity;
import com.zw.entity.TOrder;
import com.zw.entity.TOrderItem;
import com.zw.repository.OrderItemRepository;
import com.zw.repository.OrderRepository;
import com.zw.service.TOrderService;
import com.zw.util.MyDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class TOrderServiceImpl implements TOrderService {
    private static final Logger log = LoggerFactory.getLogger(TOrderServiceImpl.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * 创建订单并自动保存订单明细,
     * 鸡肋 ！！！！ 这里演示的是自动保存订单itm，我感觉很鸡肋，比正常保存思维，能节省一行代码，正常保存是
     * 是items循环保存订单id，这里直接保存订单对象，不推荐，而且item实体格式，
     * TOrderItem类
     *     @JsonIgnore
     *     @ToString.Exclude
     *     @ManyToOne(fetch = FetchType.EAGER)
     *     @Fetch(FetchMode.SELECT)
     *     @JoinColumn(
     *             name = "order_id",
     *             referencedColumnName = "id",
     *             insertable = true,
     *             updatable = true
     *     )
     *     private TOrder tOrder;
     * @param TOrder
     *
     * 注意 注意如果这样，就不能 private String orderId;字段否则报错
     *
     * TOrder 类
     * @OneToMany(mappedBy = "tOrder",  fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
     *private List<TOrderItem> items = new ArrayList<>();
     */
    public TOrder createOrderAutoSaveItem(TOrder tOrder) {
        if (tOrder.getOrderNo() == null || tOrder.getOrderNo().isEmpty()) {
            String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String randomSuffix = String.format("%04d", new Random().nextInt(10000));
            tOrder.setOrderNo("ORD" + datePart + randomSuffix);
        }
        if (tOrder.getItems() != null && !tOrder.getItems().isEmpty()) {
            for (TOrderItem item : tOrder.getItems()) {
                item.setTOrder(tOrder);
            }
        }
        orderRepository.save(tOrder);
        return tOrder;
    }
    /**
     * 推荐 ！！！ 创建订单并非自动保存订单明细
     * 生产上推荐这种实体，项目上也是这样用的
     * TOrderItem类
     *     @Column(name = "order_id", nullable = false, length = 36)
     *     private String orderId;
     *     @JsonIgnore
     *     @ToString.Exclude
     *     @ManyToOne(fetch = FetchType.EAGER)
     *     @Fetch(FetchMode.SELECT)
     *     @JoinColumn(
     *             name = "order_id",
     *             referencedColumnName = "id",
     *             insertable = false,
     *             updatable = false
     *     )
     *     private TOrder tOrder;
     * TOrder 类
     * @OneToMany(mappedBy = "tOrder",  fetch = FetchType.LAZY)
     */
    @Override
    public TOrder createOrderNoAutoSaveItem(TOrder tOrder) {
        if (tOrder.getOrderNo() == null || tOrder.getOrderNo().isEmpty()) {
            String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String randomSuffix = String.format("%04d", new Random().nextInt(10000));
            tOrder.setOrderNo("ORD" + datePart + randomSuffix);
        }
        orderRepository.save(tOrder);
        if (tOrder.getItems() != null && !tOrder.getItems().isEmpty()) {
            for (TOrderItem item : tOrder.getItems()) {
                    item.setOrderId(tOrder.getId());
            }
        }
        orderItemRepository.saveAll(tOrder.getItems());
        return tOrder;
    }
    /**
     * JPA批量插入测试（优化前），这种写法在 -Xmx100m 5万条数据就直接内存溢出,内存不限制下
     * 目前能达到1秒4,5百左右，i7+固态硬盘+32g内存+虚拟机的mysql
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void jpaBatchInsert1(List<TOrder> list) {
        log.info("jpaBatchInsert1-开始执行");
        long stime = System.currentTimeMillis();
        orderRepository.saveAll( list);
        log.info(MyDateUtil.execTime("jpaBatchInsert1-执行完成-耗时",stime));
    }

    /**
     * 高性能JPA批量插入测试前置条件
     * # 启用批处理
     * spring.jpa.properties.hibernate.jdbc.batch_size=50
     * spring.jpa.properties.hibernate.order_inserts=true
     * spring.jpa.properties.hibernate.order_updates=true
     *
     * # 禁用自动 flush（由你控制）
     * spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
     *
     * # 数据库连接参数（以 MySQL 为例）
     * spring.datasource.url=jdbc:mysql://localhost:3306/mydb?rewriteBatchedStatements=true&useServerPrepStmts=false
     *
     * JPA批量插入测试（优化后）
     * 注意：jpaBatchInsert2与jpaBatchInsert1执行时间相差不大，但是在-Xmx100m 5万条数据仍然可以正常执行
     * 执行时间相差几乎一样
     * 目前能达到1秒4,5百左右，i7+固态硬盘+32g内存+虚拟机的mysql
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void jpaBatchInsert2(List<TOrder> list) {
        log.info("jpaBatchInsert2-开始执行");
        long stime = System.currentTimeMillis();
        int batchSize = 50;
        for (int i = 0; i < list.size(); i++) {
            entityManager.persist(list.get(i));
            if ((i + 1) % batchSize == 0) {
                entityManager.flush();   // 执行批处理 SQL
                entityManager.clear();   // 清除一级缓存，释放内存
            }
        }
        // 处理剩余不足 batch 的数据
        if (CollUtil.isNotEmpty(list) && list.size() % batchSize != 0) {
            entityManager.flush();
            entityManager.clear();
        }
        log.info(MyDateUtil.execTime("jpaBatchInsert2-执行完成-耗时",stime));
    }
    /**
     * spring JDBC批量插入测试 【id需要手动设置 ，jdbc连接rewriteBatchedStatements要设置为true】
     * 目前能达到1秒4,5百左右，i7+固态硬盘+32g内存+虚拟机的mysql情况下
     * 当rewriteBatchedStatements=false时，插入 50000 条数据，耗时 38913 ms，平均每秒处理 1284.92 条【最慢的情况下都比JPA的快一倍】
     * 当rewriteBatchedStatements=true时，插入 50000 条数据，耗时 4859 ms，平均每秒处理 10290.18 条 【牛每秒1万条左右】
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void springJdbcBatchInsert(List<TOrder> list) {
        if (CollUtil.isEmpty( list)) {
            return;
        }
        // SQL 对应 t_orders 表所有需要插入的字段（按建表顺序更安全）
        String sql = "INSERT INTO t_orders (" +
                "id,order_no, user_id, status, total_amount, actual_amount, " +
                "currency, payment_method, payment_time, " +
                "shipping_address_id, consignee_name, consignee_phone, " +
                "shipping_fee, discount_amount, coupon_id, remark, " +
                "create_user_id, create_time, update_user_id, update_time, version" +
                ") VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // 分批处理：避免单次 batch 过大（推荐 500～2000 条/批）
        int batchSize = 1000;
        LocalDateTime now = LocalDateTime.now();
        Timestamp nowTs = Timestamp.valueOf(now);

        for (int i = 0; i < list.size(); i += batchSize) {
            int end = Math.min(i + batchSize, list.size());
            List<TOrder> batch = list.subList(i, end);

            jdbcTemplate.batchUpdate(sql, new org.springframework.jdbc.core.BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    TOrder order = batch.get(j);

                    String id = order.getId();
                    if (StrUtil.isEmpty(id)) {
                        id= BaseEntity.getNextId();
                    }
                    ps.setString(1, id);
                    ps.setString(2, order.getOrderNo());
                    ps.setString(3, order.getUserId());
                    ps.setInt(4, order.getStatus());
                    ps.setBigDecimal(5, order.getTotalAmount());
                    ps.setBigDecimal(6, order.getActualAmount());
                    ps.setString(7, order.getCurrency());
                    ps.setString(8, order.getPaymentMethod());
                    ps.setObject(9, order.getPaymentTime());
                    ps.setString(10, order.getShippingAddressId());
                    ps.setString(11, order.getConsigneeName());
                    ps.setString(12, order.getConsigneePhone());
                    ps.setBigDecimal(13, order.getShippingFee());
                    ps.setBigDecimal(14, order.getDiscountAmount());
                    ps.setString(15, order.getCouponId());
                    ps.setString(16, order.getRemark());
                    ps.setString(17, "创建者"); // create_user_id
                    ps.setTimestamp(18, nowTs); // create_time
                    ps.setString(19, "创建者"); // update_user_id
                    ps.setTimestamp(20, nowTs); // update_time
                    ps.setLong(21, 0L);         // version         // version = 0（初始值）
                }
                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }


}
