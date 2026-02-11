package com.zw.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class MyPage<T> {
    private List<T> list;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
}
