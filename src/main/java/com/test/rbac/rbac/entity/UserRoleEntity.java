package com.test.rbac.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.test.rbac.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 映射角色用户表
 * @author DNYY
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("blog_user_role")
public class UserRoleEntity extends BaseEntity {

    /**
     * 反序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 对应用户id
     */
    public Long userId;

    /**
     * 对应角色id
     */
    public Long roleId;
}
