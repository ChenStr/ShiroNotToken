package com.test.rbac.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 映射用户唯一标识表
 * @author DNYY
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("blog_user_token")
public class TokenEntity implements Serializable {
    /**
     * 反序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    public Long id;

    /**
     * token用户id
     */
    public Long userId;

    /**
     * 识别用户的唯一标识符
     */
    public String token;

    /**
     * 过期时间
     */
    public Date expireDate;

    /**
     * 更新时间
     */
    public Date updateDate;

    /**
     * 创建时间
     */
    public Date createDate;

}
