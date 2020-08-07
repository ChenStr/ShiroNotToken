package com.test.rbac.common.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共Entity
 * 将所有的表的公共列信息放在这个类
 */
@Data
public class BaseEntity implements Serializable {
    /**
     * id
     */
    @TableId
    public Long id;

    /**
     * 创建者
     */
    public Long creator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    public Date createDate;
}
