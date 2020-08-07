package com.test.rbac.rbac.entity;



import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.test.rbac.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 映射菜单表
 * @author DNYY
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("blog_sys_menu")
public class MenuEntity extends BaseEntity {
    /**
     * 反序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 上级菜单id,一级菜单为0
     */
    private Long pid;

    /**
     * 菜单url
     */
    private String url;

    /**
     * 授权唯一标识
     */
    private String permissions;

    /**
     * 类型 0:菜单 1:按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

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
