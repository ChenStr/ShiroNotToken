package com.test.rbac.rbac.service;

import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.BaseService;
import com.test.rbac.rbac.dto.TokenDTO;
import com.test.rbac.rbac.entity.TokenEntity;

import java.util.Map;


/**
 * Token的Service层
 * @author DNYY
 */

public interface TokenService extends BaseService<TokenEntity, TokenDTO> {
    Map<String,Object> createToken(Long userId);

    /**
     * 检测token是否过期
     * @param tokenEntity
     * @return 还可以使用为true 不能使用为 false
     */
    Boolean checkToken(TokenEntity tokenEntity);

    /**
     * 通过用户的token查看用户的角色以及拥有的权限
     * @param token
     * @return
     */
    CommonReturn see(String token);
}
