package com.test.rbac.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.impl.BaseServiceImpl;
import com.test.rbac.rbac.dao.RoleMenuDao;
import com.test.rbac.rbac.dto.*;
import com.test.rbac.rbac.entity.MenuEntity;
import com.test.rbac.rbac.entity.RoleEntity;
import com.test.rbac.rbac.entity.RoleMenuEntity;
import com.test.rbac.rbac.entity.TokenEntity;
import com.test.rbac.rbac.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author DNYY
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenuDao, RoleMenuEntity, RoleMenuDTO> implements RoleMenuService {

    @Autowired
    MenuService menuService;

    @Autowired
    RoleService roleService;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Override
    public void beforeInsert(RoleMenuDTO dto) {
        //添加创建时间与更新时间
        Date date = new Date();
        dto.setCreateDate(date);
        //添加创建人
        String token = dto.getToken();
        //通过用户的token来查找用户的id
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",token));
        //如果查找的到的话
        if(tokenEntity!=null){
            //添加修改人与创建人
            dto.setCreator(tokenEntity.getUserId());
        }
    }

    @Override
    public void beforEedit(RoleMenuDTO dto) {
        //添加更新时间
    }

    @Override
    public CommonReturn getRoleMenu(RoleMenuDTO roleMenuDTO) {
        CommonReturn result = new CommonReturn();
        Map<String,Object> data = new HashMap<>();
        //判断roleId与menuId是否有值
        if (roleMenuDTO.getRoleId()!=null){
            data.put("role_id",roleMenuDTO.getRoleId());
            List<RoleMenuDTO> roleMenuDTOS = this.select(data);
            List<Long> meunIds = new ArrayList<>();
            roleMenuDTOS.stream().forEach(i->meunIds.add(i.getMenuId()));
            QueryWrapper<MenuEntity> menuEntityQueryWrapper = new QueryWrapper<>();
            List<MenuDTO> menus = menuService.selectPage(roleMenuDTO.getPage(),roleMenuDTO.getPageSize(),menuEntityQueryWrapper.in("id",meunIds));
            if(menus==null){
                result.setAll(10001,null,"参数错误");
            }else{
                result.setAll(20000,menus,"操作成功");
            }
        }else if(roleMenuDTO.getMenuId()!=null){
            data.put("menu_id",roleMenuDTO.getMenuId());
            List<RoleMenuDTO> roleMenuDTOS = this.select(data);
            List<Long> roleIds = new ArrayList<>();
            roleMenuDTOS.stream().forEach(i->roleIds.add(i.getRoleId()));
            QueryWrapper<RoleEntity> roleEntityQueryWrapper = new QueryWrapper<>();
            List<RoleDTO> roles = roleService.selectPage(roleMenuDTO.getPage(),roleMenuDTO.getPageSize(),roleEntityQueryWrapper.in("id",roleIds));
            if(roles==null){
                result.setAll(10001,null,"参数错误");
            }else{
                result.setAll(20000,roles,"操作成功");
            }
        }else{
            result.setAll(10001,null,"参数错误");
        }
        return result;
    }

    @Override
    public CommonReturn addRoleMenu(ToListDTO<Long, Long> data) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",data.getToken()));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        //判断是否是超级管理员
        if(userDTO.getSuperAdmin()==1) {
            if(data.getData() == null || data.getData2() == null || data.getData().size() <=0 || data.getData2().size() <= 0){
                result.setAll(10001, null, "参数错误1");
            }else{
                //判断用户给的role与menu是否在数据库中真实存在
                QueryWrapper<RoleEntity> roleEntityQueryWrapper = new QueryWrapper<>();
                QueryWrapper<MenuEntity> menuEntityQueryWrapper = new QueryWrapper<>();
                List<RoleEntity> roleEs = roleService.list(roleEntityQueryWrapper.in("id",data.getData()));
                List<MenuEntity> menuEs = menuService.list(menuEntityQueryWrapper.in("id",data.getData2()));
                if (roleEs!=null && menuEs!=null && roleEs.size() > 0 && menuEs.size() > 0){
                    for (int i = 0 ; i < roleEs.size() ; i++){
                        for (int j = 0 ; j < menuEs.size() ; j++){
                            //查找角色菜单的关联表中是否有相同的信息，有的话就不用插入了
                            Map<String,Object> map = new HashMap<>();
                            RoleMenuDTO roleMenuDTO = new RoleMenuDTO();
                            roleMenuDTO.setToken(data.getToken());
                            roleMenuDTO.setRoleId(data.getData().get(i));
                            roleMenuDTO.setMenuId(data.getData2().get(j));
                            map.put("role_id",data.getData().get(i));
                            map.put("menu_id",data.getData2().get(j));
                            List<RoleMenuDTO> da = this.select(map);
                            if(da==null || da.size()<=0){
                                this.insert(roleMenuDTO);
                            }
                        }
                    }
                    result.setAll(20000,data,"操作成功");
                }else{
                    result.setAll(10001, null, "参数错误2");
                }
            }
        }else{
            result.setAll(30004,null,"用户没有权限");
        }
        return result;
    }

    @Override
    public CommonReturn delRoleMenu(ToListDTO<Long, Long> data) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",data.getToken()));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        //判断是否是超级管理员
        if(userDTO.getSuperAdmin()==1) {
            if (data.getData() == null || data.getData2() == null || data.getData().size() == 0 || data.getData2().size() == 0) {
                result.setAll(10001, null, "参数错误");
            } else {
                QueryWrapper<RoleEntity> roleEntityQueryWrapper = new QueryWrapper<>();
                QueryWrapper<MenuEntity> menuEntityQueryWrapper = new QueryWrapper<>();
                List<RoleEntity> roleEs = roleService.list(roleEntityQueryWrapper.in("id", data.getData()));
                List<MenuEntity> menuEs = menuService.list(menuEntityQueryWrapper.in("id", data.getData2()));
                if (roleEs != null && menuEs != null && roleEs.size() > 0 && menuEs.size() > 0) {
                    for (int i = 0; i < roleEs.size(); i++) {
                        for (int j = 0; j < menuEs.size(); j++) {
                            //查找角色菜单的关联表中是否有相同的信息，有的话就不用插入了
                            Map<String, Object> map = new HashMap<>();
                            map.put("role_id", data.getData().get(i));
                            map.put("menu_id", data.getData2().get(j));
                            List<RoleMenuDTO> da = this.select(map);
                            if (da != null || da.size() >= 0) {
                                List<Long> ids = new ArrayList<>();
                                da.stream().forEach(id -> ids.add(id.getId()));
                                this.deleteByIds(ids);
                            }
                        }
                    }
                    result.setAll(20000, data, "操作成功");
                }
            }
        }else{
            result.setAll(30004,null,"用户没有权限");
        }
        return result;
    }
}
