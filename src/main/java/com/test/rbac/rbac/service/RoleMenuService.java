package com.test.rbac.rbac.service;

import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.BaseService;
import com.test.rbac.rbac.dto.RoleMenuDTO;
import com.test.rbac.rbac.dto.ToListDTO;
import com.test.rbac.rbac.entity.RoleMenuEntity;

public interface RoleMenuService extends BaseService<RoleMenuEntity, RoleMenuDTO> {

    public CommonReturn getRoleMenu(RoleMenuDTO roleMenuDTO);

    public CommonReturn addRoleMenu(ToListDTO<Long, Long> data);

    public CommonReturn delRoleMenu(ToListDTO<Long, Long> data);
}
