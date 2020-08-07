package com.test.rbac.rbac.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.rbac.common.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 与User表一样的DTO
 * @author DNYY
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseDTO implements Serializable {
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
     * 用户名
     */
    public String username;

    /**
     * 密码
     */
    public String password;

    /**
     * 姓名
     */
    public String realName;

    /**
     * 头像
     */
    public String headUrl;

    /**
     * 性别 0:男 1:女 2:保密
     */
    public Integer gender;

    /**
     * 邮箱
     */
    public String email;

    /**
     * 手机号
     */
    public String mobile;

    /**
     * 是否为超级管理员 0 ：否  1：是
     */
    private Integer superAdmin;

    /**
     * 用户状态 0:停用 1:正常
     */
    public Integer status;

    /**
     * 更新者
     */
    public Long updater;

    /**
     * 更新时间
     */
    public Date updateDate;

    /**
     * 创建者
     */
    public Long creator;

    /**
     * 创建时间
     */
    public Date createDate;

    public UserDTO() {
    }

    public UserDTO(Long id, String realName) {
        this.id = id;
        this.realName = realName;
    }
}
