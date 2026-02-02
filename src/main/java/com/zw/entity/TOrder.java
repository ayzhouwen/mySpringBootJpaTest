package com.zw.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true) // ← 关键！
@Accessors(chain = true)
@Entity
@Table(name = "t_orders")
public class TOrder extends BaseEntity  {


    /** 订单编号，业务唯一，如 ORD202601290001 */
    @Column(name = "order_no", nullable = false, length = 32)
    private String orderNo;

    /** 下单用户ID（业务用户） */
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    /**
     * 订单状态：
     * 0-待支付，1-已支付，2-已发货，3-已完成，4-已取消，5-退款中
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /** 订单总金额 */
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    /** 实付金额（扣除优惠后） */
    @Column(name = "actual_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal actualAmount;

    /** 货币代码，默认 CNY */
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    /** 支付方式：alipay, wechat, bank_card 等 */
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    /** 支付时间 */
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    /** 收货地址ID */
    @Column(name = "shipping_address_id", length = 36)
    private String shippingAddressId;

    /** 收货人姓名（冗余字段） */
    @Column(name = "consignee_name", length = 50)
    private String consigneeName;

    /** 收货人电话 */
    @Column(name = "consignee_phone", length = 20)
    private String consigneePhone;

    /** 运费 */
    @Column(name = "shipping_fee", nullable = false, precision = 8, scale = 2)
    private BigDecimal shippingFee;

    /** 优惠金额 */
    @Column(name = "discount_amount", nullable = false, precision = 8, scale = 2)
    private BigDecimal discountAmount;

    /** 使用的优惠券ID */
    @Column(name = "coupon_id", length = 36)
    private String couponId;

    /** 用户下单备注 */
    @Column(name = "remark", length = 255)
    private String remark;

    /** 创建用户ID（操作人） */
    @CreatedBy
    @Column(name = "create_user_id", length = 36)
    private String createUserId;

    /** 创建时间 */
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;

    /** 最后更新用户ID */
    @Column(name = "update_user_id", length = 36)
    @LastModifiedBy
    private String updateUserId;

    /** 最后更新时间 */
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;

    /** 版本号（可用于业务版本控制或乐观锁） */
    @Column(name = "version", length = 255)
    @Version
    private Long version;

    /**
     * 订单明细列表（一对多关联）
     * mappedBy 指向 OrderItem 中的 order 字段
     */
    @OneToMany(mappedBy = "tOrder",  fetch = FetchType.LAZY)
    private List<TOrderItem> items = new ArrayList<>();
}