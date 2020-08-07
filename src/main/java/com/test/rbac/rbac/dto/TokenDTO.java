package com.test.rbac.rbac.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.rbac.common.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 与UserToken表一样的
 * @author DNYY
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDTO extends BaseDTO implements Serializable {
    /**
     * 反序列化
     */
//    private static final long serialVersionUID = 1L;

    /**
     * 识别用户的唯一标识符
     */
//    public String token;

    /**
     * id
     */
    public Long id;

    /**
     * token用户id
     */
    public Long userId;

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
