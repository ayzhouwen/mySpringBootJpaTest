package com.zw.controller;

import com.zw.entity.TOrder;
import com.zw.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private TOrderService TOrderService;
    @PostMapping
    public ResponseEntity<TOrder> createOrder(@RequestBody TOrder TOrder) {
        TOrder savedTOrder = TOrderService.createOrderAutoSaveItem(TOrder);
        return ResponseEntity.ok(savedTOrder);
    }
}
