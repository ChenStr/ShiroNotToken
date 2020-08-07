package com.test.rbac.common.service;


import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 最底层的Service层(方法基本围绕Entity)
 *
 * T为Entity类型
 *
 * @author DNYY
 */
public interface DeepService<T> extends IService<T> {

}
