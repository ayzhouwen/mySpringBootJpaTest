package com.zw.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * 用户实体类vo
 */
@Data
public class UserDto {
    private String username;
    private Integer age;
    private String email;
    private String createUserId;
    private Date createTime;
    private String updateUserId;
    private Date updateTime;
}