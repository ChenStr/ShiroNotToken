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
@TableName("blog_sys_user")
public class UserEntity extends BaseEntity {

    /**
     * 反序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 头像
     */
    private String headUrl;

    /**
     * 性别 0:男 1:女 2:保密
     */
    private Integer gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 是否为超级管理员 0 ：否  1：是
     */
    private Integer superAdmin;

    /**
     * 用户状态 0:停用 1:正常
     */
    private Integer status;

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
