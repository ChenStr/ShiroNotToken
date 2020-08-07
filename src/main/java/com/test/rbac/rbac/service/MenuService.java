package com.test.rbac.rbac.service;

import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.BaseService;
import com.test.rbac.rbac.dto.MenuDTO;
import com.test.rbac.rbac.dto.OneListDTO;
import com.test.rbac.rbac.entity.MenuEntity;


/**
 * Role的Service层
 * @author DNYY
 */
public interface MenuService extends BaseService<MenuEntity, MenuDTO> {

    public CommonReturn getMenu(MenuDTO dto);

    public CommonReturn saveMenu(MenuDTO dto);

    public CommonReturn editMenu(MenuDTO dto);

    public CommonReturn delMenu(OneListDTO<Long> ids);

    /**
     * 获取全部的菜单 分页
     * @param dto
     * @return
     */
    public CommonReturn getAllMenu(MenuDTO dto);

}
