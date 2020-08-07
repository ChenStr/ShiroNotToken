package com.test.rbac.rbac.service;

import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.BaseService;
import com.test.rbac.rbac.dto.ToListDTO;
import com.test.rbac.rbac.dto.UserRoleDTO;
import com.test.rbac.rbac.entity.UserRoleEntity;


/**
 * UserRole表的Service层
 * @author DNYY
 */
public interface UserRoleService extends BaseService<UserRoleEntity, UserRoleDTO> {

    /**
     * 查找角色所拥有的角色信息
     * @param userRoleDTO
     * @return
     */
    public CommonReturn getUserRole(UserRoleDTO userRoleDTO);

    /**
     * 给多个用户赋予多个权限
     * @param data
     * @return
     */
    public CommonReturn addUserRole(ToListDTO<Long, Long> data);

    /**
     * 解除角色与用户的绑定关系
     * @param data
     * @return
     */
    public CommonReturn delUserRole(ToListDTO<Long, Long> data);
}
