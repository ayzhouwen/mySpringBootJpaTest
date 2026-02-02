package com.zw.service;

import com.zw.entity.TOrder;

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
}