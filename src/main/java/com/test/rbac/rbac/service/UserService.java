package com.test.rbac.rbac.service;

import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.BaseService;
import com.test.rbac.rbac.dto.UserDTO;
import com.test.rbac.rbac.entity.UserEntity;

/**
 * User的Service层
 * @author DNYY
 */
public interface UserService extends BaseService<UserEntity, UserDTO> {

    /**
     * 用户登陆方法
     * @param userDTO
     * @return
     */
    CommonReturn login(UserDTO userDTO);


    /**
     * 用户注册方法
     * @param userDTO
     * @return
     */
    CommonReturn register(UserDTO userDTO);

    /**
     * 用户修改方法
     * @param userDTO
     * @return
     */
    CommonReturn editUser(UserDTO userDTO);
}
