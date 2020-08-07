package com.test.rbac.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 公共DTO内含基本所有DTO都需要的对象
 */
@Data
public class BaseDTO implements Serializable {
    /**
     * 反序列化
     */
//    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    public String token;

    /**
     * 第几页
     */
    public Integer page;

    /**
     * 每一页的数量
     */
    public Integer pageSize;
}
