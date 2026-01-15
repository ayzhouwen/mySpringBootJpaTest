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

public interface  UserVo {
    String getUsername();
    Integer getAge();
    String getEmail();
    String getCreateUserId();
    Date getCreateTime();
    String getUpdateUserId();
    Date getUpdateTime();
}