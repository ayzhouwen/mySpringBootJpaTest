package com.zw.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zw.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

/**
 * 用户实体类vo
 */
@Data
public class UserVo {
    private static final long serialVersionUID = 1L;
    protected String username;
    protected Integer age;
    protected String email;
    protected String createUserId;
    protected Date createTime;
    private String updateUserId;
    private  Date updateTime;
}