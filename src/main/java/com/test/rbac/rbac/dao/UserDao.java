package com.test.rbac.rbac.dao;


import com.test.rbac.common.dao.BaseDao;
import com.test.rbac.rbac.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户Dao层
 * @author DNYY
 */
@Mapper
public interface UserDao extends BaseDao<UserEntity> {

}
