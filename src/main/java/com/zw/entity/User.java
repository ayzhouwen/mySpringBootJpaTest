package com.zw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
/**
 * 用户实体类（JPA映射 + Lombok简化） ，在updateTime字段上中加入@Version注解，很可能会导致数据查询更新后报错
 * @Version 实体类字段类型必须为long
 */
@Data
@EqualsAndHashCode(callSuper = true) // ← 显式启用调用父类
@Entity
@Table(name = "t_user") // 指定数据库表名
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Column(name = "username", nullable = false, length = 50) // 列名、非空、长度
    protected String username;

    @Column(name = "age")
    protected Integer age;

    @Column(name = "email", length = 100)
    protected String email;

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