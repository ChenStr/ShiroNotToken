package com.test.rbac.rbac.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.rbac.common.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 映射用户表
 * @author DNYY
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO extends BaseDTO implements Serializable {

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
     * 角色名称
     */
    public String name;

    /**
     * 角色备注
     */
    public String remark;

    /**
     * 创建者
     */
    public Long creator;

    /**
     * 创建时间
     */
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
