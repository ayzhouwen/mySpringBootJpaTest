package com.zw.service.impl;

import com.zw.entity.TOrder;
import com.zw.entity.TOrderItem;
import com.zw.repository.OrderItemRepository;
import com.zw.repository.OrderRepository;
import com.zw.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class TOrderServiceImpl implements TOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

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
}
