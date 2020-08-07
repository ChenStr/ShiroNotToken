package com.test.rbac.rbac.dto;



import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.rbac.common.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 映射菜单表
 * @author DNYY
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDTO extends BaseDTO implements Serializable {
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
     * 上级菜单id,一级菜单为0
     */
    public Long pid;

    /**
     * 菜单url
     */
    public String url;

    /**
     * 授权唯一标识
     */
    public String permissions;

    /**
     * 类型 0:菜单 1:按钮
     */
    public Integer type;

    /**
     * 菜单图标
     */
    public String icon;

    /**
     * 排序
     */
    public Integer sort;

    /**
     * 创建者
     */
    public Long creator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    public Date createDate;

    /**
     * 更新者
     */
    public Long updater;

    /**
     * 更新时间
     */
    public Date updateDate;
}
