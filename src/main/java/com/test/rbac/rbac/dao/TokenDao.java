package com.test.rbac.rbac.dao;

import com.test.rbac.common.dao.BaseDao;
import com.test.rbac.rbac.entity.TokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户唯一标识符的Dao层
 * @author DNYY
 */
@Mapper
public interface TokenDao extends BaseDao<TokenEntity> {
}
