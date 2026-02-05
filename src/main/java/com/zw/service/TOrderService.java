package com.zw.service;

import com.zw.entity.TOrder;

import java.util.List;

public interface TOrderService {
    /**
     * 创建订单并自动保存订单明细
     * @param
     * @return
     */
    TOrder createOrderAutoSaveItem(TOrder tOrder);
    /**
     * 创建订单并非自动保存订单明细
     * @param
     * @return
     */
    TOrder createOrderNoAutoSaveItem(TOrder tOrder);

    /**
     *
     * @param list
     */
    void jpaBatchInsert1(List<TOrder> list);
    /**
     * @param list
     */
    void jpaBatchInsert2(List<TOrder> list);
    /**
     * spring JDBC批量插入测试
     * @param list
     */
    void springJdbcBatchInsert(List<TOrder> list);
}