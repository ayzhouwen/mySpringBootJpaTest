package com.zw.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户实体类（JPA映射 + Lombok简化）
 */
@Data // 自动生成getter、setter、toString、equals、hashCode等方法
@NoArgsConstructor // 自动生成无参构造（JPA必须）
@AllArgsConstructor // 自动生成全参构造
@Entity
@Table(name = "t_user") // 指定数据库表名
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增策略（适配MySQL）
    private Long id;

    @Column(name = "username", nullable = false, length = 50) // 列名、非空、长度
    private String username;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email", length = 100)
    private String email;

    // 无需手动编写getter/setter、构造方法、toString，Lombok自动生成
}