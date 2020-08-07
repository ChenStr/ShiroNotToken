package com.test.rbac.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.test.rbac.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("blog_role_menu")
public class RoleMenuEntity extends BaseEntity {

    /**
     * 反序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 对应角色id
     */
    public Long roleId;

    /**
     * 对应菜单id
     */
    public Long menuId;
}
