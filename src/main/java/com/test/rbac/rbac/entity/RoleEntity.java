package com.test.rbac.rbac.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.test.rbac.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 映射用户表
 * @author DNYY
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("blog_sys_role")
public class RoleEntity extends BaseEntity {

    /**
     * 反序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色备注
     */
    private String remark;

    /**
     * 更新者
     */
    private Long updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateDate;
}
