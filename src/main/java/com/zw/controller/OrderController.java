package com.zw.controller;

import cn.hutool.json.JSONObject;
import com.zw.entity.TOrder;
import com.zw.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private TOrderService tOrderService;
    @PostMapping
    public ResponseEntity<TOrder> createOrder(@RequestBody TOrder TOrder) {
        TOrder savedTOrder = tOrderService.createOrderAutoSaveItem(TOrder);
        return ResponseEntity.ok(savedTOrder);
    }

    /**
     * 获取订单分页，动态sql查询条件
     * @return
     */
    @GetMapping("/pageTest1")
    public Page<TOrder> getUserByUsername(Integer pageNum, Integer pageSize, Integer status,
                                          Integer lower, Integer upper,
                                          Date startTime,Date endTime) {
        JSONObject params = new JSONObject();
        params.put("pageNum",pageNum);
        params.put("pageSize",pageSize);
        params.put("status",status);
        params.put("lower",lower);
        params.put("upper",upper);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        Page<TOrder> page = tOrderService.getTOrderPage(params);
        return page;
    }
}
