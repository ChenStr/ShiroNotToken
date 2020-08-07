package com.test.rbac.rbac.dao;

import com.test.rbac.common.dao.BaseDao;
import com.test.rbac.rbac.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色表的Dao层
 * @author DNYY
 */
@Mapper
public interface MenuDao extends BaseDao<MenuEntity> {

}
