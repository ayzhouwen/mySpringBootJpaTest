package com.zw.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细表实体
 * 对应数据库表：t_order_items
 */
@Data
@EqualsAndHashCode(callSuper = true) // ← 关键！
@Accessors(chain = true)
@Entity
@Table(name = "t_order_items")
public class TOrderItem extends BaseEntity {
    @Column(name = "order_id", nullable = false, length = 36)
    private String orderId;
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false
    )
    private TOrder tOrder;

    /** 商品ID */
    @Column(name = "product_id", nullable = false, length = 36)
    private String productId;

    /** SKU ID（具体规格，如颜色、尺寸） */
    @Column(name = "sku_id", nullable = false, length = 36)
    private String skuId;

    /** 商品名称（冗余，防止商品删除后无法显示） */
    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    /** SKU描述，如“红色 / XL” */
    @Column(name = "sku_desc", length = 100)
    private String skuDesc;

    /** 下单时单价 */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** 购买数量 */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /** 小计 = price × quantity */
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    /** 创建用户ID */
    @Column(name = "create_user_id", length = 36)
    @CreatedBy
    private String createUserId;

    /** 创建时间 */
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;

    /** 更新用户ID */
    @Column(name = "update_user_id", length = 36)
    @LastModifiedBy
    private String updateUserId;

    /** 更新时间 */
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;

    /** 版本号 */
    @Column(name = "version", length = 255)
    @Version
    private Long version;

}