package com.zw.entity;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass // 关键：表示该类是 JPA 映射的基类，不会单独建表
@EntityListeners({AuditingEntityListener.class}) // 添加审计监听器
public  class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // 雪花ID生成器（可考虑单例或静态）
    private static final Snowflake SNOWFLAKE = IdUtil.createSnowflake(1, 1);

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 32)
    private String id;

    @PrePersist // 在持久化前自动调用
    protected void generateId() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = String.valueOf(SNOWFLAKE.nextId());
        }
    }
}
