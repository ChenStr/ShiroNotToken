package com.test.rbac.rbac.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.test.rbac.common.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 映射用户角色表
 * @author DNYY
 */
@Data
public class RoleMenuDTO extends BaseDTO implements Serializable {
    /**
     * 反序列化
     */
//    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
//    public String token;

    /**
     * id
     */
    public Long id;

    /**
     * 对应角色id
     */
    public Long roleId;

    /**
     * 对应菜单id
     */
    public Long menuId;

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
