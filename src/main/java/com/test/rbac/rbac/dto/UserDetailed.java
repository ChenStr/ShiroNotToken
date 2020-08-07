package com.test.rbac.rbac.dto;

import com.test.rbac.common.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * User对象返回给前端的格式(暂定)
 * @author DNYY
 */
@Data
public class UserDetailed extends BaseDTO implements Serializable {
    /**
     * 反序列化
     */
//    private static final long serialVersionUID = 1L;

    /**
     * 用户对象
     */
    public UserDTO userDTO;

    /**
     * 角色对象
     */
    public Set<RoleDTO> roleDTOS;

    /**
     * 权限对象
     */
    public Set<MenuDTO> menuDTOS;

}
