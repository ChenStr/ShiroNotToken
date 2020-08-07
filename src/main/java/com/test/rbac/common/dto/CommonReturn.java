package com.test.rbac.common.dto;

import lombok.Data;

/**
 * 公共方法返回值
 * @param <T>
 */
@Data
public class CommonReturn<T> {
    /**
     * 返回码
     */
    public Integer code;

    /**
     * 返回内容
     */
    public T data;

    /**
     * 返回信息
     */
    public String msg;

    public void setAll(Integer code, T data , String msg){
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
