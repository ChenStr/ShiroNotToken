package com.test.rbac.common.other;


/**
 * 枚举类错误码
 * @author DNYY
 */
public enum ErrorCode {

    ParameterError(10001,null,"参数错误"),
    Success(20000,null,"操作成功");

    /**
     * 返回码
     */
    public Integer code;

    /**
     * 返回内容
     */
    public Object data;

    /**
     * 返回信息
     */
    public String msg;

    ErrorCode(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    ErrorCode() {
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
