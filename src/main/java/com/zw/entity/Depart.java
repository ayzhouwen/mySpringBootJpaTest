package com.zw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true) // ← 显式启用调用父类
@Accessors(chain = true)
@Entity
@Table(name = "t_depart") // 指定数据库表名
public class Depart extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Column(name = "name", nullable = false, length = 50) // 列名、非空、长度
    protected String name;

    @Column(name = "code")
    protected String code;

    @Column(name = "type")
    protected String type;

    @JsonProperty("createUserID")
    @CreatedBy
    @Column(
            name = "create_user_id"
    )
    protected String createUserId;
    @CreatedDate
    @Column(name = "create_time")
    protected Date createTime;
    @JsonProperty("updateUserID")
    @LastModifiedBy
    @Column(name = "update_user_id")
    private String updateUserId;


    @Column(name = "update_time")
    @LastModifiedDate
    private  Date updateTime;
    @Column(name = "version")
    @Version
    private Long version;
}