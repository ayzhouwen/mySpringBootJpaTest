package com.zw.controller;

import cn.hutool.json.JSONObject;
import com.zw.entity.TOrder;
import com.zw.entity.dto.MyPage;
import com.zw.service.TOrderService;
import com.zw.util.MyPageUtil;
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
     *
     */
    @GetMapping("/pageTest1")
    public MyPage<TOrder> getUserByUsername(Integer pageNum, Integer pageSize, Integer status,
                                            Integer lower, Integer upper,
                                            Date startTime, Date endTime) {
        JSONObject params = new JSONObject();
        params.set("pageNum",pageNum);
        params.set("pageSize",pageSize);
        params.set("status",status);
        params.set("lower",lower);
        params.set("upper",upper);
        params.set("startTime",startTime);
        params.set("endTime",endTime);
        Page<TOrder> page = tOrderService.getTOrderPage(params);
        MyPage<TOrder> myPage= MyPageUtil.getMyPage(page);
        return myPage;
    }
}
