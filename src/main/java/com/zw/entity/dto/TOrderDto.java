package com.zw.entity.dto;
import com.zw.entity.BaseEntity;
import com.zw.entity.TOrderItem;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class TOrderDto  {
    /** 订单ID */
    private String id;
    /** 订单编号，业务唯一，如 ORD202601290001 */
    private String orderNo;

    /** 下单用户ID（业务用户） */
    private String userId;
    @Column(name = "status", nullable = false)
    private Integer status;
    private BigDecimal totalAmount;

}