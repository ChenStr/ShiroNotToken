package com.test.rbac.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.impl.BaseServiceImpl;
import com.test.rbac.rbac.dao.MenuDao;
import com.test.rbac.rbac.dto.MenuDTO;
import com.test.rbac.rbac.dto.OneListDTO;
import com.test.rbac.rbac.dto.RoleMenuDTO;
import com.test.rbac.rbac.dto.UserDTO;
import com.test.rbac.rbac.entity.MenuEntity;
import com.test.rbac.rbac.entity.TokenEntity;
import com.test.rbac.rbac.service.MenuService;
import com.test.rbac.rbac.service.RoleMenuService;
import com.test.rbac.rbac.service.TokenService;
import com.test.rbac.rbac.service.UserService;
import com.test.rbac.tools.my.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author DNYY
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuDao, MenuEntity, MenuDTO> implements MenuService {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    RoleMenuService roleMenuService;

    /**
     * 在插入后的钩子方法
     * @param dto
     */
    @Override
    public void beforeInsert(MenuDTO dto) {
        //添加创建时间与更新时间
        Date date = new Date();
        dto.setCreateDate(date);
        dto.setUpdateDate(date);
        //添加创建人与更新人
        //获取用户token
        String token = dto.getToken();
        //通过用户的token来查找用户的id
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",token));
        //如果查找的到的话
        if(tokenEntity!=null){
            //添加修改人与创建人
            dto.setCreator(tokenEntity.getUserId());
            dto.setUpdater(tokenEntity.getUserId());
        }
    }

    /**
     * 在更新后的钩子方法
     * @param dto
     */
    @Override
    public void beforEedit(MenuDTO dto) {
        //添加更新时间
        Date date = new Date();
        dto.setUpdateDate(date);
        //添加更新人
        //获取用户token
        String token = dto.getToken();
        //通过用户的token来查找用户的id
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",token));
        //如果查找的到的话
        if(tokenEntity!=null){
            //添加修改人
            dto.setUpdater(tokenEntity.getUserId());
        }
    }

    /**
     * 获取菜单方法
     * @param dto
     * @return
     */
    @Override
    public CommonReturn getMenu(MenuDTO dto) {
        CommonReturn result = new CommonReturn();
        Map<String,Object> data = MyUtils.objectToMap(dto);
        data.remove("token");
        List<MenuDTO> menus = this.select(data);
        if(menus.isEmpty()){
            result.setAll(20000,menus,"没有查找结果，建议仔细核对查找条件");
        }else{
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setToken("1");menuDTO.setId(1L);menuDTO.setPid(1L);menuDTO.setIcon("112");menuDTO.setPermissions("11");
            result.setAll(20000,menus,"查找成功");
        }
        return result;
    }

    /**
     * 添加菜单方法
     * @param dto
     * @return
     */
    @Override
    public CommonReturn saveMenu(MenuDTO dto) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token", dto.getToken()));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        if (userDTO.getSuperAdmin() == 1) {
            //判断数据库里是否有相同的名字的菜单了
            Map<String,Object> map = new HashMap<>();
            map.put("url",dto.getUrl());
            map.put("permissions",dto.getPermissions());
            List<MenuDTO> menus = this.select(map);
            if(menus==null || menus.size() == 0){
                //设置用户不允许修改的属性
                dto.setUpdateDate(null);
                dto.setUpdater(null);
                dto.setCreateDate(null);
                dto.setCreator(null);
                try {
                    this.insert(dto);
                    result.setAll(20000, dto, "操作成功");
                } catch (Exception e) {
                    result.setAll(10001, null, "参数错误");
                }
            }else{
                result.setAll(20000, dto, "重复操作");
            }
        } else {
            result.setAll(30004, null, "用户没有权限");
        }
        return result;
    }

    /**
     * 编辑菜单方法
     * @param dto
     * @return
     */
    @Override
    public CommonReturn editMenu(MenuDTO dto) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",dto.getToken()));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        if(userDTO.getSuperAdmin()==1){
            //设置用户不允许修改的属性
            dto.setUpdateDate(null);
            dto.setUpdater(null);
            dto.setCreateDate(null);
            dto.setCreator(null);
            dto.setPermissions(null);
            try{
                this.edit(dto);
                result.setAll(20000,dto,"操作成功");
            }catch (Exception e){
                result.setAll(10001,null,"参数错误");
            }
        }else{
            result.setAll(30004,null,"没有权限");
        }
        return result;
    }

    @Override
    public CommonReturn delMenu(OneListDTO<Long> ids) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",ids.getToken()));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        if(userDTO.getSuperAdmin()==1){
            if (ids.getData()!=null && ids.getData().size() > 0){
                List<Long> menuIds = ids.getData();
                //找到MENU表与连接表里的数据
                List<MenuDTO> menus = this.selectByIds(menuIds);
                List<Long> roleMenuIds = new ArrayList<>();
                for (int i = 0 ; i < menus.size() ; i++){
                    Map<String,Object> data = new HashMap<>();
                    data.put("menu_id",menus.get(i).getId());
                    List<RoleMenuDTO> roleMenuDTOS = roleMenuService.select(data);
                    roleMenuDTOS.stream().forEach(j->roleMenuIds.add(j.getId()));
                }
                //删除相关数据
                if(roleMenuIds!=null){
                    this.deleteByIds(menuIds);
                    roleMenuService.deleteByIds(roleMenuIds);
                    result.setAll(20000,ids,"操作成功");
                }else{
                    result.setAll(10001,null,"参数错误");
                }
            }else{
                result.setAll(10001,null,"参数错误");
            }
        }else{
            result.setAll(30004,null,"没有权限");
        }
        return result;
    }

    @Override
    public CommonReturn getAllMenu(MenuDTO dto) {
        CommonReturn result = new CommonReturn();
        List<MenuDTO> menuDTOS = this.selectPage(dto.getPage(),dto.getPageSize(),null);
        if (menuDTOS==null){
            result.setAll(10001,null,"参数错误");
        }else{
            result.setAll(20000,menuDTOS,"查找成功");
        }
        return result;
    }


}
