package com.test.rbac.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;
import java.util.Map;

/**
 * 浮桥Service层 (方法基本围绕DTO)
 *
 * @author DNYY
 *
 * T为Entity类型
 *
 * D为DTO类型
 */
public interface BaseService<T,D> extends DeepService<T> {

    public void insert(D dto);

    public D selectById (Long id);

    public List<D> selectByIds(List<Long> ids);

    public void edit(D dto);

    public Boolean deleteById(Long id);

    public Boolean deleteByIds(List<Long> ids);

    public List<D> select(Map<String,Object> data);

    public List<D> select(QueryWrapper<T> wrapper);

    public List<D> selectPage(Object page,Object pageSize, QueryWrapper<T> queryWrapper);

}
